package com.example.ui.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppScreen
import com.example.data.model.DispatchLog
import com.example.data.model.DropZoneType
import com.example.data.model.Employee
import com.example.data.model.ShiftType
import com.example.data.model.Vendor
import com.example.data.model.WatermarkConfig
import com.example.data.repository.CateringRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CateringUiState(
  val currentScreen: AppScreen = AppScreen.DISPATCH,
  val activeShift: ShiftType = ShiftType.SIANG,
  val selectedSector: String = "Sector North (Pit 01-04)",
  val activeVendor: Vendor,
  val allVendors: List<Vendor>,
  val employees: List<Employee>,
  val shiftEmployees: List<Employee>,
  val totalPax: Int,
  val totalCuti: Int,
  val mes1Pax: Int,
  val mes2Pax: Int,
  val mes3Pax: Int,
  val mes4Pax: Int,
  val siteOfficePax: Int,
  val mes3CutiNames: List<String>,
  val quickNotes: Set<String>,
  val watermarkConfig: WatermarkConfig,
  val dispatchLogs: List<DispatchLog>,
  val isVendorEditModalOpen: Boolean = false,
  val isWaConfirmModalOpen: Boolean = false,
  val isShareModalOpen: Boolean = false,
  val isSideDrawerOpen: Boolean = false,
  val monitoringViewMode: MonitoringViewMode = MonitoringViewMode.SPREADSHEET
)

enum class MonitoringViewMode {
  SPREADSHEET,
  CARDS
}

class CateringViewModel(
  private val repository: CateringRepository = CateringRepository()
) : ViewModel() {

  private val _currentScreen = MutableStateFlow(AppScreen.DISPATCH)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _isVendorEditModalOpen = MutableStateFlow(false)
  val isVendorEditModalOpen: StateFlow<Boolean> = _isVendorEditModalOpen.asStateFlow()

  private val _isWaConfirmModalOpen = MutableStateFlow(false)
  val isWaConfirmModalOpen: StateFlow<Boolean> = _isWaConfirmModalOpen.asStateFlow()

  private val _isShareModalOpen = MutableStateFlow(false)
  val isShareModalOpen: StateFlow<Boolean> = _isShareModalOpen.asStateFlow()

  private val _isSideDrawerOpen = MutableStateFlow(false)
  val isSideDrawerOpen: StateFlow<Boolean> = _isSideDrawerOpen.asStateFlow()

  private val _monitoringViewMode = MutableStateFlow(MonitoringViewMode.SPREADSHEET)
  val monitoringViewMode: StateFlow<MonitoringViewMode> = _monitoringViewMode.asStateFlow()

  private val _toastEvent = MutableSharedFlow<String>()
  val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

  val uiState: StateFlow<CateringUiState> = combine(
    _currentScreen,
    repository.activeShift,
    repository.selectedSector,
    repository.vendors,
    repository.activeVendorId,
    repository.employees,
    repository.quickNotes,
    repository.watermarkConfig,
    repository.dispatchLogs,
    _isVendorEditModalOpen,
    _isWaConfirmModalOpen,
    _isShareModalOpen,
    _isSideDrawerOpen,
    _monitoringViewMode
  ) { args ->
    val screen = args[0] as AppScreen
    val shift = args[1] as ShiftType
    val sector = args[2] as String
    @Suppress("UNCHECKED_CAST")
    val vendors = args[3] as List<Vendor>
    val vendorId = args[4] as String
    @Suppress("UNCHECKED_CAST")
    val allEmployees = args[5] as List<Employee>
    @Suppress("UNCHECKED_CAST")
    val notes = args[6] as Set<String>
    val watermark = args[7] as WatermarkConfig
    @Suppress("UNCHECKED_CAST")
    val logs = args[8] as List<DispatchLog>
    val isVendorModal = args[9] as Boolean
    val isWaModal = args[10] as Boolean
    val isShareModal = args[11] as Boolean
    val isDrawer = args[12] as Boolean
    val viewMode = args[13] as MonitoringViewMode

    val activeVendor = vendors.firstOrNull { it.id == vendorId } ?: vendors.first()

    val shiftEmps = when (shift) {
      ShiftType.PAGI -> allEmployees.filter { it.activeInPagi }
      ShiftType.SIANG -> allEmployees.filter { it.activeInSiang }
      ShiftType.MALAM -> allEmployees.filter { it.activeInMalam }
    }

    val totalPax = shiftEmps.count { !it.isCuti }
    val totalCuti = shiftEmps.count { it.isCuti }

    val mes1Pax = shiftEmps.filter { it.zone == DropZoneType.MES_1 }.count { !it.isCuti }
    val mes2Pax = shiftEmps.filter { it.zone == DropZoneType.MES_2 }.count { !it.isCuti }
    val mes3Pax = shiftEmps.filter { it.zone == DropZoneType.MES_3 }.count { !it.isCuti }
    val mes4Pax = shiftEmps.filter { it.zone == DropZoneType.MES_4 }.count { !it.isCuti }
    val siteOfficePax = shiftEmps.filter { it.zone == DropZoneType.SITE_OFFICE }.count { !it.isCuti }

    val mes3Cuti = shiftEmps.filter { it.zone == DropZoneType.MES_3 && it.isCuti }.map { it.name }

    CateringUiState(
      currentScreen = screen,
      activeShift = shift,
      selectedSector = sector,
      activeVendor = activeVendor,
      allVendors = vendors,
      employees = allEmployees,
      shiftEmployees = shiftEmps,
      totalPax = totalPax,
      totalCuti = totalCuti,
      mes1Pax = mes1Pax,
      mes2Pax = mes2Pax,
      mes3Pax = mes3Pax,
      mes4Pax = mes4Pax,
      siteOfficePax = siteOfficePax,
      mes3CutiNames = mes3Cuti,
      quickNotes = notes,
      watermarkConfig = watermark,
      dispatchLogs = logs,
      isVendorEditModalOpen = isVendorModal,
      isWaConfirmModalOpen = isWaModal,
      isShareModalOpen = isShareModal,
      isSideDrawerOpen = isDrawer,
      monitoringViewMode = viewMode
    )
  }.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    CateringUiState(
      activeVendor = repository.vendors.value.first(),
      allVendors = repository.vendors.value,
      employees = repository.employees.value,
      shiftEmployees = repository.getEmployeesForShift(ShiftType.SIANG),
      totalPax = 54,
      totalCuti = 2,
      mes1Pax = 5,
      mes2Pax = 1,
      mes3Pax = 16,
      mes4Pax = 13,
      siteOfficePax = 19,
      mes3CutiNames = listOf("Arir", "Afnan"),
      quickNotes = emptySet(),
      watermarkConfig = WatermarkConfig(),
      dispatchLogs = repository.dispatchLogs.value
    )
  )

  fun setScreen(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun setActiveShift(shift: ShiftType) {
    repository.setActiveShift(shift)
  }

  fun setSelectedSector(sector: String) {
    repository.setSelectedSector(sector)
  }

  fun setActiveVendorId(vendorId: String) {
    repository.setActiveVendorId(vendorId)
  }

  fun toggleEmployeeCuti(employeeId: String) {
    repository.toggleEmployeeCuti(employeeId)
    viewModelScope.launch {
      _toastEvent.emit("Status Roster Karyawan diperbarui")
    }
  }

  fun toggleQuickNote(note: String) {
    repository.toggleQuickNote(note)
  }

  fun setWatermarkScale(scale: Float, label: String) {
    repository.setWatermarkScale(scale, label)
  }

  fun setWatermarkOpacity(opacity: Float, label: String) {
    repository.setWatermarkOpacity(opacity, label)
  }

  fun setMonitoringViewMode(mode: MonitoringViewMode) {
    _monitoringViewMode.value = mode
  }

  fun openVendorEditModal() {
    _isVendorEditModalOpen.value = true
  }

  fun closeVendorEditModal() {
    _isVendorEditModalOpen.value = false
  }

  fun openWaConfirmModal() {
    _isWaConfirmModalOpen.value = true
  }

  fun closeWaConfirmModal() {
    _isWaConfirmModalOpen.value = false
  }

  fun openShareModal() {
    _isShareModalOpen.value = true
  }

  fun closeShareModal() {
    _isShareModalOpen.value = false
  }

  fun openSideDrawer() {
    _isSideDrawerOpen.value = true
  }

  fun closeSideDrawer() {
    _isSideDrawerOpen.value = false
  }

  fun saveVendor(vendor: Vendor) {
    repository.updateVendor(vendor)
    _isVendorEditModalOpen.value = false
    viewModelScope.launch {
      _toastEvent.emit("✓ Data vendor ${vendor.name} berhasil diperbarui!")
    }
  }

  fun generateWhatsAppMessage(): String {
    val state = uiState.value
    val shift = state.activeShift
    val vendor = state.activeVendor

    val cutiNoteStr = if (state.totalCuti > 0) "${state.totalCuti} Cuti/Off-Site" else "0 Cuti"

    val mes3Info = if (state.mes3CutiNames.isNotEmpty()) {
      "4. MES 3: ${state.mes3Pax} Box (Total ${state.mes3Pax + state.mes3CutiNames.size}, ${state.mes3CutiNames.size} Cuti: ${state.mes3CutiNames.joinToString(" & ")})"
    } else {
      "4. MES 3: ${state.mes3Pax} Box"
    }

    val deadline = when (shift) {
      ShiftType.PAGI -> "Harap drop di Mes sebelum 06.45 WITA"
      ShiftType.SIANG -> "Harap drop di Mes 3 sebelum 11.30 WITA"
      ShiftType.MALAM -> "Harap drop di Pos Utama sebelum 18.15 WITA"
    }

    val sb = StringBuilder()
    sb.append("Halo ${vendor.name} (${vendor.pic}), berikut REKAP DROP ZONE CATERING ${shift.label.uppercase()} (01 Okt 2026 - ${shift.scheduleTime}):\n")
    sb.append("---------------------------------\n")
    sb.append("📍 TOTAL KEBUTUHAN: ${state.totalPax} Box ($cutiNoteStr)\n\n")
    sb.append("📦 RINCIAN DROP POINT:\n")
    sb.append("1. SITE (Office & Pit): ${state.siteOfficePax} Box\n")
    sb.append("2. MES 1: ${state.mes1Pax} Box\n")
    sb.append("3. MES 2: ${state.mes2Pax} Box (Pak Tina)\n")
    sb.append("$mes3Info\n")
    sb.append("5. MES 4: ${state.mes4Pax} Box\n\n")
    sb.append("⚠️ CATATAN KHUSUS:\n")
    sb.append("- $deadline\n")
    sb.append("- Box diberi label nama masing-masing kamar\n")
    if (vendor.specialNote.isNotBlank()) {
      sb.append("- ${vendor.specialNote}\n")
    }
    state.quickNotes.forEach { note ->
      sb.append("- $note\n")
    }
    sb.append("Terima kasih!\n- Pengawas Site")

    return sb.toString()
  }

  fun copyRekapText(context: Context) {
    val text = generateWhatsAppMessage()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Rekap Drop Zone Catering", text)
    clipboard.setPrimaryClip(clip)
    viewModelScope.launch {
      _toastEvent.emit("Teks Rekap Disalin ke Clipboard")
    }
  }

  fun launchWhatsAppDirect(context: Context) {
    val state = uiState.value
    val phoneClean = state.activeVendor.phone.replace(Regex("[^0-9]"), "").let {
      if (it.startsWith("0")) "62" + it.substring(1) else it
    }
    val text = generateWhatsAppMessage()
    val uri = Uri.parse("https://wa.me/$phoneClean?text=${Uri.encode(text)}")
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
      flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    try {
      context.startActivity(intent)
      _isWaConfirmModalOpen.value = false
      viewModelScope.launch {
        _toastEvent.emit("Membuka WhatsApp Manifest...")
      }
    } catch (_: Exception) {
      val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
      }
      try {
        context.startActivity(Intent.createChooser(sendIntent, "Kirim Rekap Catering"))
      } catch (_: Exception) {
        viewModelScope.launch {
          _toastEvent.emit("Teks Rekap disalin (WhatsApp tidak terpasang)")
        }
      }
    }
  }

  fun simulateSuccessfulDispatch() {
    val state = uiState.value
    repository.markShiftConfirmed(state.activeShift)
    repository.addDispatchLog(
      DispatchLog(
        id = "log-${System.currentTimeMillis()}",
        title = "Shift ${state.activeShift.label} (${state.totalPax} Pax)",
        desc = "Terkirim ${state.activeShift.scheduleTime} • ${state.activeVendor.name}",
        time = state.activeShift.scheduleTime,
        status = "Terkonfirmasi",
        isConfirmed = true
      )
    )
    _isWaConfirmModalOpen.value = false
    viewModelScope.launch {
      _toastEvent.emit("✓ Dispatch ${state.activeShift.label} Terkirim ke Vendor Catering!")
    }
  }

  fun simulateDownloadPdf() {
    viewModelScope.launch {
      _toastEvent.emit("Sedang membuat file PDF Slip Serah Terima...")
      kotlinx.coroutines.delay(1000)
      _toastEvent.emit("PDF Berhasil Diunduh: BA-CAT-MIP-20261001.pdf")
    }
  }

  fun shareDocumentChannel(channelName: String) {
    _isShareModalOpen.value = false
    viewModelScope.launch {
      _toastEvent.emit("Terkirim ke: $channelName")
    }
  }
}
