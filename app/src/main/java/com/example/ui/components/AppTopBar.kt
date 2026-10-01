package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.DarkSurfaceBright
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.TextOnSurface
import com.example.ui.theme.TextOnSurfaceVariant

@Composable
fun AppTopBar(
  onOpenDrawer: () -> Unit,
  onFastBroadcastClick: () -> Unit,
  onNotificationClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseAlpha"
  )

  Row(
    modifier = modifier
      .fillMaxWidth()
      .height(64.dp)
      .background(DarkCanvasBase)
      .padding(horizontal = 12.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    // Left: Drawer button + Logo + Title
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f, fill = false)
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceContainer)
          .clickable(onClick = onOpenDrawer)
          .testTag("tune_drawer_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Tune,
          contentDescription = "Open Site and Shift Menu",
          tint = TextOnSurface,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      // Logo thumbnail
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(RoundedCornerShape(6.dp))
          .background(Color.White.copy(alpha = 0.1f))
          .padding(4.dp),
        contentAlignment = Alignment.Center
      ) {
        MucoindoLogo(modifier = Modifier.size(32.dp))
      }

      Spacer(modifier = Modifier.width(8.dp))

      Column(modifier = Modifier.weight(1f, fill = false)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(7.dp)
              .alpha(pulseAlpha)
              .background(EmeraldSecondary, CircleShape)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "PIT NORTH • ACTIVE",
            color = EmeraldSecondary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "Monitoring",
            color = TextOnSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "PT. MUCOINDO PRAKASA",
            color = TextOnSurfaceVariant,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
      }
    }

    // Right: Action buttons
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      // WA Broadcast button with active badge
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceContainer)
          .clickable(onClick = onFastBroadcastClick)
          .testTag("top_broadcast_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "WhatsApp Instant Broadcast",
          tint = EmeraldSecondary,
          modifier = Modifier.size(19.dp)
        )
        Box(
          modifier = Modifier
            .size(6.dp)
            .align(Alignment.TopEnd)
            .padding(top = 7.dp, end = 7.dp)
            .background(EmeraldSecondary, CircleShape)
        )
      }

      // Dark Mode toggle
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.DarkMode,
          contentDescription = "Theme",
          tint = TextOnSurfaceVariant,
          modifier = Modifier.size(19.dp)
        )
      }

      // Notifications with badge
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceContainer)
          .clickable(onClick = onNotificationClick)
          .testTag("top_notification_button"),
        contentAlignment = Alignment.Center
      ) {
        BadgedBox(
          badge = {
            Badge(
              containerColor = Color(0xFFEF4444),
              contentColor = Color.White
            ) {
              Text("3", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = TextOnSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      // Profile avatar
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(AmberPrimary),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "User Profile",
          tint = Color(0xFF472A00),
          modifier = Modifier.size(19.dp)
        )
      }
    }
  }
}
