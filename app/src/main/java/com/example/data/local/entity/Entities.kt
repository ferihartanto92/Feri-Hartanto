package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity storing catering delivery records (Berita Acara Serah Terima Catering).
 */
@Entity(tableName = "delivery_records")
data class DeliveryRecordEntity(
  @PrimaryKey val id: String, // e.g. "BA-CAT/MIP/2026/10/01-S02"
  val shift: String, // Pagi, Siang, Malam
  val date: String, // 01 Okt 2026
  val arrivalTime: String, // 11:15 WITA
  val vendorName: String,
  val driverName: String,
  val receiverName: String,
  val receiverId: String, // MIP-9902
  val boxTemperature: String, // 68°C
  val qcStatus: String, // QC HSE Lolos
  val totalPax: Int,
  val menuText: String,
  val batchNumber: String,
  val cryptoHash: String,
  val status: String = "TERVALIDASI",
  val timestamp: Long = System.currentTimeMillis()
)

/**
 * Entity storing breakdown allocation per drop zone for each catering delivery.
 */
@Entity(tableName = "drop_zone_logs")
data class DropZoneLogEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val deliveryRecordId: String,
  val shift: String,
  val zoneName: String, // Mes 1, Mes 2, Mes 3, Mes 4, Site Office & Pit North
  val locationDetail: String,
  val paxCount: Int,
  val cutiNotes: String = "",
  val verificationStatus: String = "Lengkap",
  val timestamp: Long = System.currentTimeMillis()
)

/**
 * Entity storing local personnel roster and cuti/off-site status.
 */
@Entity(tableName = "roster_personnel")
data class RosterPersonnelEntity(
  @PrimaryKey val id: String,
  val name: String,
  val zone: String, // SITE_OFFICE, MES_1, MES_2, MES_3, MES_4
  val isCuti: Boolean = false,
  val note: String = "",
  val activeInPagi: Boolean = true,
  val activeInSiang: Boolean = true,
  val activeInMalam: Boolean = true
)

/**
 * Entity storing vendor contract information locally for offline access.
 */
@Entity(tableName = "vendor_contracts")
data class VendorContractEntity(
  @PrimaryKey val id: String,
  val name: String,
  val phone: String,
  val pic: String,
  val status: String,
  val specialNote: String,
  val isPrimary: Boolean = false
)
