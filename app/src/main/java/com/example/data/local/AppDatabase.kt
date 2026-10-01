package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.DeliveryRecordDao
import com.example.data.local.dao.DropZoneLogDao
import com.example.data.local.dao.RosterPersonnelDao
import com.example.data.local.dao.VendorContractDao
import com.example.data.local.entity.DeliveryRecordEntity
import com.example.data.local.entity.DropZoneLogEntity
import com.example.data.local.entity.RosterPersonnelEntity
import com.example.data.local.entity.VendorContractEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    DeliveryRecordEntity::class,
    DropZoneLogEntity::class,
    RosterPersonnelEntity::class,
    VendorContractEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun deliveryRecordDao(): DeliveryRecordDao
  abstract fun dropZoneLogDao(): DropZoneLogDao
  abstract fun rosterPersonnelDao(): RosterPersonnelDao
  abstract fun vendorContractDao(): VendorContractDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "catering_offline.db"
        )
          .fallbackToDestructiveMigration()
          .addCallback(DatabasePrepopulateCallback())
          .build()
        INSTANCE = instance
        instance
      }
    }

    private class DatabasePrepopulateCallback : RoomDatabase.Callback() {
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        INSTANCE?.let { database ->
          CoroutineScope(Dispatchers.IO).launch {
            prepopulateInitialData(database)
          }
        }
      }
    }

    suspend fun prepopulateInitialData(db: AppDatabase) {
      // 1. Initial Official Delivery Record (BA-CAT/MIP/2026/10/01-S02)
      val initialDelivery = DeliveryRecordEntity(
        id = "BA-CAT/MIP/2026/10/01-S02",
        shift = "Siang",
        date = "01 Okt 2026",
        arrivalTime = "11:15 WITA",
        vendorName = "PT Berkah Boga Mandiri",
        driverName = "Bpk. Rustam (LV Pit-04)",
        receiverName = "Pak Tina / Pengawas",
        receiverId = "MIP-9902",
        boxTemperature = "68°C",
        qcStatus = "QC HSE Lolos",
        totalPax = 54,
        menuText = "Nasi Putih + Ayam Bakar Madu + Tumis Buncis + Buah Semangka",
        batchNumber = "#02-SNG",
        cryptoHash = "#BA-MIP-8841-A9",
        status = "TERVALIDASI"
      )
      db.deliveryRecordDao().insertDeliveryRecord(initialDelivery)

      // 2. Initial Drop Zone Logs for the delivery
      val dropZoneLogs = listOf(
        DropZoneLogEntity(
          deliveryRecordId = initialDelivery.id,
          shift = "Siang",
          zoneName = "Mes 1",
          locationDetail = "Pit North Camp Block A",
          paxCount = 5,
          cutiNotes = "",
          verificationStatus = "Lengkap"
        ),
        DropZoneLogEntity(
          deliveryRecordId = initialDelivery.id,
          shift = "Siang",
          zoneName = "Mes 2",
          locationDetail = "Pit North Camp Block B",
          paxCount = 1,
          cutiNotes = "Pak Tina",
          verificationStatus = "Lengkap"
        ),
        DropZoneLogEntity(
          deliveryRecordId = initialDelivery.id,
          shift = "Siang",
          zoneName = "Mes 3",
          locationDetail = "Pit North Camp Block C",
          paxCount = 16,
          cutiNotes = "2 Cuti: Arir, Afnan dikecualikan",
          verificationStatus = "Lengkap"
        ),
        DropZoneLogEntity(
          deliveryRecordId = initialDelivery.id,
          shift = "Siang",
          zoneName = "Mes 4",
          locationDetail = "Camp Supervisor & Safety",
          paxCount = 13,
          cutiNotes = "",
          verificationStatus = "Lengkap"
        ),
        DropZoneLogEntity(
          deliveryRecordId = initialDelivery.id,
          shift = "Siang",
          zoneName = "Site Office & Pit North",
          locationDetail = "Main Workshop & Control Tower",
          paxCount = 19,
          cutiNotes = "",
          verificationStatus = "Lengkap"
        )
      )
      db.dropZoneLogDao().insertDropZoneLogs(dropZoneLogs)

      // 3. Prepopulate Vendors
      val vendors = listOf(
        VendorContractEntity(
          id = "berkah",
          name = "PT Berkah Boga Mandiri",
          phone = "+62 812-3498-8921",
          pic = "Ibu Siti Rahma / Pengelola",
          status = "Kontrak Utama Aktif",
          specialNote = "Wajib antar box tertutup rapat & tepat waktu di Site Office & Mes 1-4",
          isPrimary = true
        ),
        VendorContractEntity(
          id = "borneo",
          name = "CV Borneo Mining Catering",
          phone = "+62 821-5582-7719",
          pic = "Pak Hendra Gunawan",
          status = "Vendor Cadangan",
          specialNote = "Armada siaga cuaca basah & jalur hauling basah",
          isPrimary = false
        ),
        VendorContractEntity(
          id = "citarasa",
          name = "PT Cita Rasa Tambang Nusantara",
          phone = "+62 813-8821-4430",
          pic = "Ibu Melani",
          status = "Diet Medis & VIP",
          specialNote = "Kemasan bertanda diet medis khusus",
          isPrimary = false
        )
      )
      db.vendorContractDao().insertAllVendors(vendors)

      // 4. Prepopulate Roster Personnel
      val roster = mutableListOf<RosterPersonnelEntity>()
      var id = 1

      // Mes 1
      listOf("Ica", "Kristoper", "Imam", "Qafin", "Ariza", "Budi", "Laode", "Freya").forEach { name ->
        val inSiang = name in listOf("Imam", "Qafin", "Ariza", "Budi", "Laode")
        roster.add(
          RosterPersonnelEntity(
            id = "emp-${id++}",
            name = name,
            zone = "MES_1",
            isCuti = false,
            activeInPagi = true,
            activeInSiang = inSiang,
            activeInMalam = true
          )
        )
      }

      // Mes 2
      roster.add(
        RosterPersonnelEntity(
          id = "emp-${id++}",
          name = "Pak Tina",
          zone = "MES_2",
          isCuti = false,
          note = "Pengawas Logistik",
          activeInPagi = true,
          activeInSiang = true,
          activeInMalam = true
        )
      )

      // Mes 3
      listOf(
        "Lambang", "Feri", "Mukti Ali", "Dian", "Budi Irawan",
        "Arir", // Cuti
        "Alfath", "Didin", "Lukman",
        "Afnan", // Cuti
        "Rifai", "Riska", "Agung", "Adam", "Herman", "Rafi HSE", "Rafi", "Purnama", "Yuli"
      ).forEach { name ->
        val isCuti = (name == "Arir" || name == "Afnan")
        val inPagi = name !in listOf("Agung", "Adam", "Herman", "Rafi HSE", "Rafi", "Purnama")
        val inMalam = name in listOf("Lambang", "Feri", "Mukti Ali", "Dian", "Budi Irawan", "Arir", "Alfath", "Lukman", "Afnan", "Rifai", "Riska")
        roster.add(
          RosterPersonnelEntity(
            id = "emp-${id++}",
            name = name,
            zone = "MES_3",
            isCuti = isCuti,
            note = if (isCuti) "Cuti / Off-Site" else "",
            activeInPagi = inPagi,
            activeInSiang = true,
            activeInMalam = inMalam
          )
        )
      }

      // Mes 4
      listOf(
        "Saidtulah", "Aji Kurniawan", "Akhid", "Slamet Hidayat",
        "Rurlan", "Andi Ba", "Bambang Jumaidi", "Mat Ali",
        "Ahmad Darazatul", "Fian Setiawan", "Karwandi", "Milham Bahir", "Sudirman"
      ).forEach { name ->
        roster.add(
          RosterPersonnelEntity(
            id = "emp-${id++}",
            name = name,
            zone = "MES_4",
            isCuti = false,
            activeInPagi = true,
            activeInSiang = true,
            activeInMalam = true
          )
        )
      }

      // Site Office & Pit North
      listOf(
        "Andi Jarman QC", "OP Excha", "Op DT", "Dinda", "Siti",
        "Jojo", "Khafi", "Harianti", "Ahmad HSE", "Vobi",
        "Ani", "Rirfani", "Dodi", "Roni", "Safia", "Adjie"
      ).forEach { name ->
        roster.add(
          RosterPersonnelEntity(
            id = "emp-${id++}",
            name = name,
            zone = "SITE_OFFICE",
            isCuti = false,
            activeInPagi = false,
            activeInSiang = true,
            activeInMalam = false
          )
        )
      }

      listOf("Agung (OT)", "Herman (OT)", "Didin (OT)", "Yuli (OT)").forEach { name ->
        roster.add(
          RosterPersonnelEntity(
            id = "emp-${id++}",
            name = name,
            zone = "SITE_OFFICE",
            isCuti = false,
            activeInPagi = false,
            activeInSiang = false,
            activeInMalam = true
          )
        )
      }

      db.rosterPersonnelDao().insertAllPersonnel(roster)
    }
  }
}
