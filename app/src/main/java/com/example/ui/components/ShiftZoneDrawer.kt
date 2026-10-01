package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShiftType
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkSurfaceBright
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceContainerHigh
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.TextOnSurface
import com.example.ui.theme.TextOnSurfaceVariant

@Composable
fun ShiftZoneDrawer(
  activeShift: ShiftType,
  selectedSector: String,
  onShiftSelect: (ShiftType) -> Unit,
  onSectorSelect: (String) -> Unit,
  onClose: () -> Unit,
  modifier: Modifier = Modifier
) {
  val sectors = listOf(
    "Sector North (Pit 01-04)",
    "Sector Central Workshop",
    "Haul Road Drop Post"
  )

  Column(
    modifier = modifier
      .fillMaxHeight()
      .width(310.dp)
      .background(DarkSurfaceContainerHigh)
      .padding(20.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.SwapHoriz,
          contentDescription = null,
          tint = AmberPrimary,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Shift & Zone Setup",
          color = TextOnSurface,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold
        )
      }
      IconButton(onClick = onClose, modifier = Modifier.size(36.dp)) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Close Drawer",
          tint = TextOnSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Active Shift section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(DarkSurfaceContainer)
        .padding(14.dp)
    ) {
      Text(
        text = "ACTIVE SHIFT",
        color = TextOnSurfaceVariant,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(10.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ShiftType.values().forEach { shift ->
          val isSelected = (shift == activeShift)
          Box(
            modifier = Modifier
              .weight(1f)
              .height(38.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) AmberPrimary else DarkSurfaceBright)
              .clickable { onShiftSelect(shift) }
              .testTag("drawer_shift_${shift.label.lowercase()}"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = shift.label,
              color = if (isSelected) Color(0xFF472A00) else TextOnSurface,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Mining Sector section
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(DarkSurfaceContainer)
        .padding(14.dp)
    ) {
      Text(
        text = "MINING SECTOR",
        color = TextOnSurfaceVariant,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp
      )
      Spacer(modifier = Modifier.height(10.dp))
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        sectors.forEach { sector ->
          val isSelected = (sector == selectedSector)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) DarkSurfaceBright else Color.Transparent)
              .clickable { onSectorSelect(sector) }
              .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = sector,
              color = if (isSelected) TextOnSurface else TextOnSurfaceVariant,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            )
            if (isSelected) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = EmeraldSecondary,
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }
  }
}
