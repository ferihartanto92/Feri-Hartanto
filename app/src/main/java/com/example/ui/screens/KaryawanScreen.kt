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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DropZoneType
import com.example.data.model.Employee
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonError
import com.example.ui.theme.CrimsonTertiary
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.DarkSurfaceBright
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceContainerHigh
import com.example.ui.theme.DarkSurfaceContainerLow
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.TextOnSurface
import com.example.ui.theme.TextOnSurfaceVariant
import com.example.ui.viewmodel.CateringUiState

@Composable
fun KaryawanScreen(
  state: CateringUiState,
  onToggleCuti: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var statusFilter by remember { mutableStateOf("ALL") } // ALL, ON_SITE, CUTI
  var selectedZoneFilter by remember { mutableStateOf<DropZoneType?>(null) }

  val allEmps = state.employees

  val filtered = allEmps.filter { emp ->
    val matchSearch = searchQuery.isBlank() || emp.name.contains(searchQuery, ignoreCase = true)
    val matchStatus = when (statusFilter) {
      "ON_SITE" -> !emp.isCuti
      "CUTI" -> emp.isCuti
      else -> true
    }
    val matchZone = selectedZoneFilter == null || emp.zone == selectedZoneFilter
    matchSearch && matchStatus && matchZone
  }

  val totalOnSite = allEmps.count { !it.isCuti }
  val totalCuti = allEmps.count { it.isCuti }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkCanvasBase)
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // 1. Header & Summary Stats
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(DarkSurfaceContainer)
        .padding(12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AmberPrimary.copy(alpha = 0.2f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Badge,
            contentDescription = null,
            tint = AmberPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Karyawan & Roster Mess",
            color = TextOnSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${allEmps.size} Total Personel Terdaftar",
            color = TextOnSurfaceVariant,
            fontSize = 10.sp
          )
        }
      }

      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(EmeraldSecondary.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "$totalOnSite ON-SITE",
            color = EmeraldSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(CrimsonError.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "$totalCuti CUTI",
            color = CrimsonTertiary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // 2. Status Segment Tabs (Semua, ON-Site, Cuti)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(DarkSurfaceContainer)
        .padding(3.dp),
      horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      listOf("ALL" to "Semua", "ON_SITE" to "Hadir On-Site", "CUTI" to "Cuti / Off-Site").forEach { (code, label) ->
        val isSel = (statusFilter == code)
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSel) AmberPrimary else Color.Transparent)
            .clickable { statusFilter = code }
            .padding(vertical = 7.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = label,
            color = if (isSel) Color(0xFF472A00) else TextOnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // 3. Zone Filters Scrollable Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      val isAllZone = (selectedZoneFilter == null)
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(if (isAllZone) AmberPrimary.copy(alpha = 0.25f) else DarkSurfaceContainer)
          .border(1.dp, if (isAllZone) AmberPrimary else BorderSubtle, RoundedCornerShape(8.dp))
          .clickable { selectedZoneFilter = null }
          .padding(horizontal = 10.dp, vertical = 5.dp)
      ) {
        Text(
          text = "Semua Lokasi",
          color = if (isAllZone) AmberPrimary else TextOnSurfaceVariant,
          fontSize = 11.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      DropZoneType.values().forEach { zone ->
        val isSel = (selectedZoneFilter == zone)
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSel) AmberPrimary.copy(alpha = 0.25f) else DarkSurfaceContainer)
            .border(1.dp, if (isSel) AmberPrimary else BorderSubtle, RoundedCornerShape(8.dp))
            .clickable { selectedZoneFilter = zone }
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Text(
            text = zone.fullName,
            color = if (isSel) AmberPrimary else TextOnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    // 4. Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      placeholder = { Text("Cari nama personel...", fontSize = 12.sp) },
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

    // 5. Employee List with Toggle
    LazyColumn(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(filtered, key = { it.id }) { emp ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (emp.isCuti) CrimsonError.copy(alpha = 0.12f) else DarkSurfaceContainer)
            .border(
              1.dp,
              if (emp.isCuti) CrimsonError.copy(alpha = 0.4f) else BorderSubtle,
              RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                  if (emp.isCuti) CrimsonError.copy(alpha = 0.2f)
                  else EmeraldSecondary.copy(alpha = 0.2f)
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (emp.isCuti) Icons.Default.EventBusy else Icons.Default.Person,
                contentDescription = null,
                tint = if (emp.isCuti) CrimsonError else EmeraldSecondary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = emp.name,
                color = TextOnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = emp.zone.fullName,
                  color = TextOnSurfaceVariant,
                  fontSize = 10.sp
                )
                if (emp.isCuti) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Text(
                    text = "• Cuti / Off-Site",
                    color = CrimsonTertiary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }

          // Cuti toggle switch
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = if (emp.isCuti) "CUTI" else "HADIR",
              color = if (emp.isCuti) CrimsonTertiary else EmeraldSecondary,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(end = 6.dp)
            )
            Switch(
              checked = !emp.isCuti,
              onCheckedChange = { onToggleCuti(emp.id) },
              colors = SwitchDefaults.colors(
                checkedThumbColor = EmeraldSecondary,
                checkedTrackColor = EmeraldSecondary.copy(alpha = 0.3f),
                uncheckedThumbColor = CrimsonError,
                uncheckedTrackColor = CrimsonError.copy(alpha = 0.3f)
              ),
              modifier = Modifier.testTag("switch_cuti_${emp.id}")
            )
          }
        }
      }
    }

    // Explanatory note
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(DarkSurfaceContainerLow)
        .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Info,
        contentDescription = null,
        tint = EmeraldSecondary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "Mengubah status cuti langsung memperbarui kalkulasi Pax di WhatsApp Dispatch dan Berita Acara.",
        color = TextOnSurfaceVariant,
        fontSize = 10.sp
      )
    }
  }
}
