package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DropZoneType
import com.example.data.model.Employee
import com.example.data.model.ShiftType
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.CrimsonTertiary
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.DarkSurfaceBright
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceContainerHigh
import com.example.ui.theme.DarkSurfaceContainerHighest
import com.example.ui.theme.DarkSurfaceContainerLow
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.TextOnSurface
import com.example.ui.theme.TextOnSurfaceVariant
import com.example.ui.viewmodel.CateringUiState
import com.example.ui.viewmodel.MonitoringViewMode

@Composable
fun MonitoringScreen(
  state: CateringUiState,
  onShiftSelect: (ShiftType) -> Unit,
  onViewModeSelect: (MonitoringViewMode) -> Unit,
  onToggleCuti: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkCanvasBase)
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // 1. Shift Selector Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(DarkSurfaceContainer)
        .padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      ShiftType.values().forEach { shift ->
        val isSelected = shift == state.activeShift
        val pax = when (shift) {
          ShiftType.PAGI -> 35
          ShiftType.SIANG -> state.totalPax
          ShiftType.MALAM -> 37
        }
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) AmberPrimary else Color.Transparent)
            .clickable { onShiftSelect(shift) }
            .padding(vertical = 8.dp)
            .testTag("mon_shift_${shift.label.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = shift.label,
              color = if (isSelected) Color(0xFF472A00) else TextOnSurfaceVariant,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "$pax Box • ${shift.scheduleTime}",
              color = if (isSelected) Color(0xFF472A00).copy(alpha = 0.8f) else TextOnSurfaceVariant.copy(alpha = 0.6f),
              fontSize = 9.sp
            )
          }
        }
      }
    }

    // 2. View Mode Toggle (Spreadsheet Matrix vs Field Cards) & Search
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Toggle
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceContainer)
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (state.monitoringViewMode == MonitoringViewMode.SPREADSHEET) AmberPrimary else Color.Transparent)
            .clickable { onViewModeSelect(MonitoringViewMode.SPREADSHEET) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("toggle_spreadsheet_view"),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.TableChart,
              contentDescription = null,
              tint = if (state.monitoringViewMode == MonitoringViewMode.SPREADSHEET) Color(0xFF472A00) else TextOnSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Tabel Spreadsheet",
              color = if (state.monitoringViewMode == MonitoringViewMode.SPREADSHEET) Color(0xFF472A00) else TextOnSurfaceVariant,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (state.monitoringViewMode == MonitoringViewMode.CARDS) AmberPrimary else Color.Transparent)
            .clickable { onViewModeSelect(MonitoringViewMode.CARDS) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("toggle_cards_view"),
          contentAlignment = Alignment.Center
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.ViewAgenda,
              contentDescription = null,
              tint = if (state.monitoringViewMode == MonitoringViewMode.CARDS) Color(0xFF472A00) else TextOnSurfaceVariant,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Kartu Drop Zone",
              color = if (state.monitoringViewMode == MonitoringViewMode.CARDS) Color(0xFF472A00) else TextOnSurfaceVariant,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // Legend Strip matching Image 1.png
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(12.dp)
              .background(CrimsonError, RoundedCornerShape(2.dp))
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(text = "Off Site (Cuti)", color = TextOnSurfaceVariant, fontSize = 9.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(12.dp)
              .background(Color.White, RoundedCornerShape(2.dp))
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(text = "ON Site", color = TextOnSurfaceVariant, fontSize = 9.sp)
        }
      }
    }

    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Cari nama karyawan di roster...", fontSize = 12.sp) },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = null,
          tint = TextOnSurfaceVariant,
          modifier = Modifier.size(18.dp)
        )
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = DarkSurfaceContainer,
        unfocusedContainerColor = DarkSurfaceContainer,
        focusedBorderColor = AmberPrimary,
        unfocusedBorderColor = BorderSubtle,
        focusedTextColor = TextOnSurface,
        unfocusedTextColor = TextOnSurface
      ),
      shape = RoundedCornerShape(10.dp),
      singleLine = true
    )

    // 3. Content: Either Spreadsheet Matrix View or Field Cards View
    if (state.monitoringViewMode == MonitoringViewMode.SPREADSHEET) {
      SpreadsheetMatrixView(
        state = state,
        searchQuery = searchQuery,
        onToggleCuti = onToggleCuti,
        modifier = Modifier.weight(1f)
      )
    } else {
      FieldCardsView(
        state = state,
        searchQuery = searchQuery,
        onToggleCuti = onToggleCuti,
        modifier = Modifier.weight(1f)
      )
    }
  }
}

// Faithful replication of Image 1.png spreadsheet view
@Composable
fun SpreadsheetMatrixView(
  state: CateringUiState,
  searchQuery: String,
  onToggleCuti: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val hScrollState = rememberScrollState()
  val vScrollState = rememberScrollState()

  val shift = state.activeShift
  val emps = state.shiftEmployees

  // Group by zone
  val siteList = emps.filter { it.zone == DropZoneType.SITE_OFFICE }
  val mes1List = emps.filter { it.zone == DropZoneType.MES_1 }
  val mes2List = emps.filter { it.zone == DropZoneType.MES_2 }
  val mes3List = emps.filter { it.zone == DropZoneType.MES_3 }
  val mes4List = emps.filter { it.zone == DropZoneType.MES_4 }

  val maxRows = maxOf(
    siteList.size,
    mes1List.size,
    mes2List.size,
    mes3List.size,
    mes4List.size
  ).coerceAtLeast(14)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(Color.White)
      .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
  ) {
    // Title inside spreadsheet canvas
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = 10.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "MONITORING DAILY DROP ZONE CATERING",
          color = Color.Black,
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          fontFamily = FontFamily.Monospace
        )
        Text(
          text = "Catering ${shift.label} (01 Oktober 2026 - ${shift.scheduleTime})",
          color = Color(0xFF334155),
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        )
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "TTL ${state.totalPax}",
          color = Color.Black,
          fontSize = 12.sp,
          fontWeight = FontWeight.ExtraBold,
          fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Cuti ${state.totalCuti}",
          color = CrimsonError,
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold,
          fontFamily = FontFamily.Monospace
        )
      }
    }

    // Table Content with horizontal scroll
    Box(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .horizontalScroll(hScrollState)
    ) {
      Column(
        modifier = Modifier
          .verticalScroll(vScrollState)
      ) {
        // Table Header: site | mes 1 | mes 2 | mes 3 | mes 4 (Yellow gold style from Image 1.png)
        Row(
          modifier = Modifier
            .background(Color(0xFFFFC000))
            .border(1.dp, Color.Black)
        ) {
          val colWidth = 105.dp
          listOf(
            "site",
            "mes 1",
            "mes 2",
            "mes 3",
            "mes 4"
          ).forEach { colName ->
            Box(
              modifier = Modifier
                .width(colWidth)
                .border(1.dp, Color.Black)
                .padding(vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = colName,
                color = Color.Black,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }

        // Table Rows
        val colWidth = 105.dp
        for (rowIndex in 0 until maxRows) {
          Row(
            modifier = Modifier
              .background(Color.White)
              .border(0.5.dp, Color.LightGray)
          ) {
            val cellSite = siteList.getOrNull(rowIndex)
            val cellMes1 = mes1List.getOrNull(rowIndex)
            val cellMes2 = mes2List.getOrNull(rowIndex)
            val cellMes3 = mes3List.getOrNull(rowIndex)
            val cellMes4 = mes4List.getOrNull(rowIndex)

            listOf(cellSite, cellMes1, cellMes2, cellMes3, cellMes4).forEach { emp ->
              val isMatch = searchQuery.isNotEmpty() && emp?.name?.contains(searchQuery, ignoreCase = true) == true
              val isCuti = emp?.isCuti == true
              val cellBg = when {
                isCuti -> Color(0xFFFF0000) // Red cell for cuti from Image 1.png
                isMatch -> Color(0xFFFEF08A) // Yellow highlight for search match
                else -> Color.White
              }
              val cellTextColor = when {
                isCuti -> Color.White
                else -> Color.Black
              }

              Box(
                modifier = Modifier
                  .width(colWidth)
                  .height(26.dp)
                  .border(0.5.dp, Color(0xFF000000))
                  .background(cellBg)
                  .clickable(enabled = emp != null) {
                    if (emp != null) onToggleCuti(emp.id)
                  }
                  .padding(horizontal = 4.dp),
                contentAlignment = Alignment.CenterStart
              ) {
                if (emp != null) {
                  Text(
                    text = emp.name,
                    color = cellTextColor,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = if (isCuti) FontWeight.ExtraBold else FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }
          }
        }

        // Table Footer: Green Totals Row from Image 1.png
        Row(
          modifier = Modifier
            .background(Color(0xFF92D050))
            .border(1.dp, Color.Black)
        ) {
          val colWidth = 105.dp
          val counts = listOf(
            siteList.count { !it.isCuti },
            mes1List.count { !it.isCuti },
            mes2List.count { !it.isCuti },
            mes3List.count { !it.isCuti },
            mes4List.count { !it.isCuti }
          )

          counts.forEach { count ->
            Box(
              modifier = Modifier
                .width(colWidth)
                .border(1.dp, Color.Black)
                .padding(vertical = 4.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "$count",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }
    }

    // Footnote
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color(0xFFF1F5F9))
        .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
      Text(
        text = "* Klik nama karyawan untuk toggle Cuti/On-Site langsung. Total & manifest otomatis tersinkronisasi.",
        color = Color(0xFF475569),
        fontSize = 9.sp,
        fontFamily = FontFamily.SansSerif
      )
    }
  }
}

// Field Cards View with interactive toggles
@Composable
fun FieldCardsView(
  state: CateringUiState,
  searchQuery: String,
  onToggleCuti: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val emps = state.shiftEmployees
  val filtered = if (searchQuery.isBlank()) emps else emps.filter {
    it.name.contains(searchQuery, ignoreCase = true) || it.zone.fullName.contains(searchQuery, ignoreCase = true)
  }

  val zones = listOf(
    DropZoneType.MES_1,
    DropZoneType.MES_2,
    DropZoneType.MES_3,
    DropZoneType.MES_4,
    DropZoneType.SITE_OFFICE
  )

  LazyColumn(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(zones) { zone ->
      val zoneEmps = filtered.filter { it.zone == zone }
      val onSiteCount = zoneEmps.count { !it.isCuti }
      val cutiCount = zoneEmps.count { it.isCuti }

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(DarkSurfaceContainer)
          .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AmberPrimary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.MeetingRoom,
                contentDescription = null,
                tint = AmberPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = zone.fullName,
                color = TextOnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = zone.locationDetail,
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(EmeraldSecondary.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "$onSiteCount Pax",
                color = EmeraldSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            if (cutiCount > 0) {
              Spacer(modifier = Modifier.width(4.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(CrimsonError.copy(alpha = 0.2f))
                  .padding(horizontal = 6.dp, vertical = 3.dp)
              ) {
                Text(
                  text = "$cutiCount Cuti",
                  color = CrimsonTertiary,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        // List of workers in this zone
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          zoneEmps.forEach { emp ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(if (emp.isCuti) CrimsonError.copy(alpha = 0.15f) else DarkSurfaceContainerHigh)
                .clickable { onToggleCuti(emp.id) }
                .padding(horizontal = 10.dp, vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = emp.name,
                color = if (emp.isCuti) CrimsonTertiary else TextOnSurface,
                fontSize = 11.sp,
                fontWeight = if (emp.isCuti) FontWeight.Bold else FontWeight.Medium
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (emp.isCuti) CrimsonError else EmeraldSecondary)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = if (emp.isCuti) "CUTI (Off-Site)" else "ON-SITE",
                  color = if (emp.isCuti) Color.White else Color(0xFF003824),
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }
  }
}
