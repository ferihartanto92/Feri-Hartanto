package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.DeliveryRecordEntity
import com.example.data.local.entity.DropZoneLogEntity
import com.example.data.local.entity.VendorContractEntity
import com.example.data.model.DispatchLog
import com.example.data.model.DropZoneType
import com.example.data.model.Employee
import com.example.data.model.ShiftType
import com.example.data.model.Vendor
import com.example.data.model.WatermarkConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CateringRepository(
  private val database: AppDatabase? = null
) {
  private val scope = CoroutineScope(Dispatchers.IO)

  private val _employees = MutableStateFlow<List<Employee>>(initialEmployees())
  val employees: StateFlow<List<Employee>> = _employees.asStateFlow()

  private val _activeShift = MutableStateFlow(ShiftType.SIANG)
  val activeShift: StateFlow<ShiftType> = _activeShift.asStateFlow()

  private val _vendors = MutableStateFlow<List<Vendor>>(initialVendors())
  val vendors: StateFlow<List<Vendor>> = _vendors.asStateFlow()

  private val _activeVendorId = MutableStateFlow("berkah")
  val activeVendorId: StateFlow<String> = _activeVendorId.asStateFlow()

  private val _quickNotes = MutableStateFlow<Set<String>>(emptySet())
  val quickNotes: StateFlow<Set<String>> = _quickNotes.asStateFlow()

  private val _watermarkConfig = MutableStateFlow(WatermarkConfig())
  val watermarkConfig: StateFlow<WatermarkConfig> = _watermarkConfig.asStateFlow()

  private val _dispatchLogs = MutableStateFlow<List<DispatchLog>>(initialLogs())
  val dispatchLogs: StateFlow<List<DispatchLog>> = _dispatchLogs.asStateFlow()

  private val _selectedSector = MutableStateFlow("Sector North (Pit 01-04)")
  val selectedSector: StateFlow<String> = _selectedSector.asStateFlow()

  init {
    database?.let { db ->
      scope.launch {
        if (db.vendorContractDao().countVendors() == 0) {
          AppDatabase.prepopulateInitialData(db)
        }
      }
    }
  }

  fun setActiveShift(shift: ShiftType) {
    _activeShift.value = shift
  }

  fun setSelectedSector(sector: String) {
    _selectedSector.value = sector
  }

  fun setActiveVendorId(vendorId: String) {
    _activeVendorId.value = vendorId
  }

  fun toggleEmployeeCuti(employeeId: String) {
    _employees.update { list ->
      list.map { emp ->
        if (emp.id == employeeId) {
          val updatedCuti = !emp.isCuti
          database?.let { db ->
            scope.launch {
              db.rosterPersonnelDao().updateCutiStatus(employeeId, updatedCuti)
            }
          }
          emp.copy(isCuti = updatedCuti)
        } else emp
      }
    }
  }

  fun updateEmployeeZone(employeeId: String, newZone: DropZoneType) {
    _employees.update { list ->
      list.map { emp ->
        if (emp.id == employeeId) emp.copy(zone = newZone) else emp
      }
    }
  }

  fun updateVendor(updated: Vendor) {
    _vendors.update { list ->
      list.map { if (it.id == updated.id) updated else it }
    }
    database?.let { db ->
      scope.launch {
        db.vendorContractDao().insertOrUpdateVendor(
          VendorContractEntity(
            id = updated.id,
            name = updated.name,
            phone = updated.phone,
            pic = updated.pic,
            status = updated.status,
            specialNote = updated.specialNote,
            isPrimary = updated.isPrimary
          )
        )
      }
    }
  }

  fun toggleQuickNote(note: String) {
    _quickNotes.update { current ->
      if (current.contains(note)) current - note else current + note
    }
  }

  fun setWatermarkScale(scale: Float, label: String) {
    _watermarkConfig.update { it.copy(scale = scale, scaleLabel = label) }
  }

  fun setWatermarkOpacity(opacity: Float, label: String) {
    _watermarkConfig.update { it.copy(opacity = opacity, opacityLabel = label) }
  }

  fun addDispatchLog(log: DispatchLog) {
    _dispatchLogs.update { listOf(log) + it }
  }

  fun markShiftConfirmed(shift: ShiftType) {
    _dispatchLogs.update { list ->
      list.map {
        if (it.title.contains(shift.label, ignoreCase = true)) {
          it.copy(status = "Terkonfirmasi", isConfirmed = true)
        } else it
      }
    }
  }

  // --- Local Room Database Methods for Offline Access ---

  suspend fun saveDeliveryRecordLocally(
    record: DeliveryRecordEntity,
    zoneLogs: List<DropZoneLogEntity>
  ) {
    database?.let { db ->
      db.deliveryRecordDao().insertDeliveryRecord(record)
      db.dropZoneLogDao().insertDropZoneLogs(zoneLogs)
    }
  }

  fun getOfflineDeliveries(): Flow<List<DeliveryRecordEntity>> {
    return database?.deliveryRecordDao()?.getAllDeliveryRecords() ?: emptyFlow()
  }

  fun getOfflineDropZoneLogs(): Flow<List<DropZoneLogEntity>> {
    return database?.dropZoneLogDao()?.getAllDropZoneLogs() ?: emptyFlow()
  }

  // --- Calculations for shift and zone ---
  fun getEmployeesForShift(shift: ShiftType): List<Employee> {
    val all = _employees.value
    return when (shift) {
      ShiftType.PAGI -> all.filter { it.activeInPagi }
      ShiftType.SIANG -> all.filter { it.activeInSiang }
      ShiftType.MALAM -> all.filter { it.activeInMalam }
    }
  }

  fun getZoneEmployees(shift: ShiftType, zone: DropZoneType): List<Employee> {
    return getEmployeesForShift(shift).filter { it.zone == zone }
  }

  fun getZonePaxCount(shift: ShiftType, zone: DropZoneType): Int {
    return getZoneEmployees(shift, zone).count { !it.isCuti }
  }

  fun getTotalPax(shift: ShiftType): Int {
    return getEmployeesForShift(shift).count { !it.isCuti }
  }

  fun getTotalCuti(shift: ShiftType): Int {
    return getEmployeesForShift(shift).count { it.isCuti }
  }

  fun getCutiNamesInZone(shift: ShiftType, zone: DropZoneType): List<String> {
    return getZoneEmployees(shift, zone).filter { it.isCuti }.map { it.name }
  }

  companion object {
    private fun initialEmployees(): List<Employee> {
      val list = mutableListOf<Employee>()
      var idGen = 1

      // Mes 1 (Pit North Camp Block A)
      val mes1Pagi = listOf("Ica", "Kristoper", "Imam", "Qafin", "Ariza", "Budi", "Laode", "Freya")
      mes1Pagi.forEach { name ->
        val inSiangMes1 = name in listOf("Imam", "Qafin", "Ariza", "Budi", "Laode")
        list.add(
          Employee(
            id = "emp-${idGen++}",
            name = name,
            zone = DropZoneType.MES_1,
            isCuti = false,
            activeInPagi = true,
            activeInSiang = inSiangMes1,
            activeInMalam = true
          )
        )
      }

      // Mes 2 (Pit North Camp Block B)
      list.add(
        Employee(
          id = "emp-${idGen++}",
          name = "Pak Tina",
          zone = DropZoneType.MES_2,
          isCuti = false,
          note = "Pengawas Logistik",
          activeInPagi = true,
          activeInSiang = true,
          activeInMalam = true
        )
      )

      // Mes 3 (Pit North Camp Block C)
      val mes3Names = listOf(
        "Lambang", "Feri", "Mukti Ali", "Dian", "Budi Irawan",
        "Arir", // Cuti
        "Alfath", "Didin", "Lukman",
        "Afnan", // Cuti
        "Rifai", "Riska", "Agung", "Adam", "Herman", "Rafi HSE", "Rafi", "Purnama", "Yuli"
      )
      mes3Names.forEach { name ->
        val isCuti = (name == "Arir" || name == "Afnan")
        val inPagi = name !in listOf("Agung", "Adam", "Herman", "Rafi HSE", "Rafi", "Purnama")
        val inMalam = name in listOf("Lambang", "Feri", "Mukti Ali", "Dian", "Budi Irawan", "Arir", "Alfath", "Lukman", "Afnan", "Rifai", "Riska")
        list.add(
          Employee(
            id = "emp-${idGen++}",
            name = name,
            zone = DropZoneType.MES_3,
            isCuti = isCuti,
            note = if (isCuti) "Cuti / Off-Site" else "",
            activeInPagi = inPagi,
            activeInSiang = true,
            activeInMalam = inMalam
          )
        )
      }

      // Mes 4 (Camp Supervisor & Safety)
      val mes4Names = listOf(
        "Saidtulah", "Aji Kurniawan", "Akhid", "Slamet Hidayat",
        "Rurlan", "Andi Ba", "Bambang Jumaidi", "Mat Ali",
        "Ahmad Darazatul", "Fian Setiawan", "Karwandi", "Milham Bahir", "Sudirman"
      )
      mes4Names.forEach { name ->
        list.add(
          Employee(
            id = "emp-${idGen++}",
            name = name,
            zone = DropZoneType.MES_4,
            isCuti = false,
            activeInPagi = true,
            activeInSiang = true,
            activeInMalam = true
          )
        )
      }

      // Site Office & Pit North (Day shift team: 19 pax)
      val siteOnlyNames = listOf(
        "Andi Jarman QC", "OP Excha", "Op DT", "Dinda", "Siti",
        "Jojo", "Khafi", "Harianti", "Ahmad HSE", "Vobi",
        "Ani", "Rirfani", "Dodi", "Roni", "Safia", "Adjie"
      )
      siteOnlyNames.forEach { name ->
        list.add(
          Employee(
            id = "emp-${idGen++}",
            name = name,
            zone = DropZoneType.SITE_OFFICE,
            isCuti = false,
            activeInPagi = false,
            activeInSiang = true,
            activeInMalam = false
          )
        )
      }

      // 4 overtime / night front guards at Site
      val siteMalamOvertime = listOf("Agung (OT)", "Herman (OT)", "Didin (OT)", "Yuli (OT)")
      siteMalamOvertime.forEach { name ->
        list.add(
          Employee(
            id = "emp-${idGen++}",
            name = name,
            zone = DropZoneType.SITE_OFFICE,
            isCuti = false,
            activeInPagi = false,
            activeInSiang = false,
            activeInMalam = true
          )
        )
      }

      return list
    }

    private fun initialVendors(): List<Vendor> {
      return listOf(
        Vendor(
          id = "berkah",
          name = "PT Berkah Boga Mandiri",
          phone = "+62 812-3498-8921",
          pic = "Ibu Siti Rahma / Pengelola",
          status = "Kontrak Utama Aktif",
          specialNote = "Wajib antar box tertutup rapat & tepat waktu di Site Office & Mes 1-4",
          isPrimary = true
        ),
        Vendor(
          id = "borneo",
          name = "CV Borneo Mining Catering",
          phone = "+62 821-5582-7719",
          pic = "Pak Hendra Gunawan",
          status = "Vendor Cadangan",
          specialNote = "Armada siaga cuaca basah & jalur hauling basah",
          isPrimary = false
        ),
        Vendor(
          id = "citarasa",
          name = "PT Cita Rasa Tambang Nusantara",
          phone = "+62 813-8821-4430",
          pic = "Ibu Melani",
          status = "Diet Medis & VIP",
          specialNote = "Kemasan bertanda diet medis khusus",
          isPrimary = false
        )
      )
    }

    private fun initialLogs(): List<DispatchLog> {
      return listOf(
        DispatchLog(
          id = "log-1",
          title = "Draft Siang - Siap Kirim",
          desc = "54 Pax • Drop Zone 1-4",
          time = "11:18 WITA",
          status = "Siap Kirim",
          isConfirmed = false
        ),
        DispatchLog(
          id = "log-2",
          title = "Shift Pagi (35 Pax)",
          desc = "Terkirim 06:15 WITA • Ibu Siti Rahma",
          time = "06:15 WITA",
          status = "Terkonfirmasi",
          isConfirmed = true
        ),
        DispatchLog(
          id = "log-3",
          title = "Shift Malam (37 Pax)",
          desc = "30 Sep • Terkirim 17:40 WITA",
          time = "17:40 WITA",
          status = "Selesai",
          isConfirmed = true
        )
      )
    }
  }
}
