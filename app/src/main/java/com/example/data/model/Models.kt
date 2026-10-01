package com.example.data.model

enum class ShiftType(
  val label: String,
  val scheduleTime: String,
  val defaultPax: Int
) {
  PAGI("Pagi", "06.00 WITA", 35),
  SIANG("Siang", "11.00 WITA", 54),
  MALAM("Malam", "17.00 WITA", 37)
}

enum class DropZoneType(
  val shortName: String,
  val fullName: String,
  val locationDetail: String
) {
  SITE_OFFICE("Site", "Site Office & Pit North", "Main Workshop & Control Tower"),
  MES_1("Mes 1", "Mes 1", "Pit North Camp Block A"),
  MES_2("Mes 2", "Mes 2", "Pit North Camp Block B"),
  MES_3("Mes 3", "Mes 3", "Pit North Camp Block C"),
  MES_4("Mes 4", "Mes 4", "Camp Supervisor & Safety")
}

data class Employee(
  val id: String,
  val name: String,
  val zone: DropZoneType,
  val isCuti: Boolean = false,
  val note: String = "",
  val activeInPagi: Boolean = true,
  val activeInSiang: Boolean = true,
  val activeInMalam: Boolean = true
)

data class Vendor(
  val id: String,
  val name: String,
  val phone: String,
  val pic: String,
  val status: String,
  val specialNote: String,
  val isPrimary: Boolean = false
)

data class WatermarkConfig(
  val scale: Float = 1.0f,
  val scaleLabel: String = "100%",
  val opacity: Float = 0.12f,
  val opacityLabel: String = "12% Standar"
)

data class DispatchLog(
  val id: String,
  val title: String,
  val desc: String,
  val time: String,
  val status: String,
  val isConfirmed: Boolean = false
)

enum class AppScreen(val label: String, val badge: String? = null) {
  MONITORING("Monitoring"),
  ORDERS("Orders", "54"),
  DISPATCH("Dispatch"),
  KARYAWAN("Karyawan")
}
