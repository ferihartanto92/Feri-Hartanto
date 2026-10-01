package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MarkChatRead
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppScreen
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.TextOnSurfaceVariant

@Composable
fun BottomNavBar(
  currentScreen: AppScreen,
  onScreenSelect: (AppScreen) -> Unit,
  orderBadgeCount: Int,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(DarkCanvasBase.copy(alpha = 0.95f))
      .navigationBarsPadding()
      .height(68.dp)
      .padding(horizontal = 8.dp),
    horizontalArrangement = Arrangement.SpaceAround,
    verticalAlignment = Alignment.CenterVertically
  ) {
    NavTabItem(
      label = "Monitoring",
      icon = Icons.Default.GridView,
      isSelected = currentScreen == AppScreen.MONITORING,
      badgeText = null,
      onClick = { onScreenSelect(AppScreen.MONITORING) },
      testTag = "nav_monitoring"
    )

    NavTabItem(
      label = "Orders",
      icon = Icons.Default.Restaurant,
      isSelected = currentScreen == AppScreen.ORDERS,
      badgeText = "$orderBadgeCount",
      onClick = { onScreenSelect(AppScreen.ORDERS) },
      testTag = "nav_orders"
    )

    NavTabItem(
      label = "Dispatch",
      icon = Icons.Default.MarkChatRead,
      isSelected = currentScreen == AppScreen.DISPATCH,
      badgeText = null,
      onClick = { onScreenSelect(AppScreen.DISPATCH) },
      testTag = "nav_dispatch"
    )

    NavTabItem(
      label = "Karyawan",
      icon = Icons.Default.Badge,
      isSelected = currentScreen == AppScreen.KARYAWAN,
      badgeText = null,
      onClick = { onScreenSelect(AppScreen.KARYAWAN) },
      testTag = "nav_karyawan"
    )
  }
}

@Composable
private fun NavTabItem(
  label: String,
  icon: ImageVector,
  isSelected: Boolean,
  badgeText: String?,
  onClick: () -> Unit,
  testTag: String
) {
  Column(
    modifier = Modifier
      .size(width = 68.dp, height = 56.dp)
      .clickable(onClick = onClick)
      .testTag(testTag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    if (badgeText != null) {
      BadgedBox(
        badge = {
          Badge(
            containerColor = AmberPrimary,
            contentColor = Color(0xFF472A00)
          ) {
            Text(badgeText, fontSize = 9.sp, fontWeight = FontWeight.Bold)
          }
        }
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = if (isSelected) AmberPrimary else TextOnSurfaceVariant,
          modifier = Modifier.size(24.dp)
        )
      }
    } else {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = if (isSelected) AmberPrimary else TextOnSurfaceVariant,
        modifier = Modifier.size(24.dp)
      )
    }

    Text(
      text = label,
      color = if (isSelected) AmberPrimary else TextOnSurfaceVariant,
      fontSize = 10.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}
