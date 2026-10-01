package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PhotoSizeSelectSmall
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShiftType
import com.example.data.model.Vendor
import com.example.ui.components.MucoindoLogo
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.AmberPrimaryContainer
import com.example.ui.theme.BorderOutlineVariant
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CrimsonTertiary
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.DarkSurfaceBright
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceContainerHigh
import com.example.ui.theme.DarkSurfaceContainerHighest
import com.example.ui.theme.DarkSurfaceContainerLow
import com.example.ui.theme.DarkSurfaceContainerLowest
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.EmeraldSecondaryContainer
import com.example.ui.theme.TextOnSurface
import com.example.ui.theme.TextOnSurfaceVariant
import com.example.ui.theme.WaDarkGreen
import com.example.ui.viewmodel.CateringUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsappDispatchScreen(
  state: CateringUiState,
  onShiftSelect: (ShiftType) -> Unit,
  onVendorSelect: (String) -> Unit,
  onOpenVendorEdit: () -> Unit,
  onCloseVendorEdit: () -> Unit,
  onSaveVendor: (Vendor) -> Unit,
  onToggleNote: (String) -> Unit,
  onSetWatermarkScale: (Float, String) -> Unit,
  onSetWatermarkOpacity: (Float, String) -> Unit,
  onCopyRekap: (Context) -> Unit,
  onOpenWaConfirm: () -> Unit,
  onCloseWaConfirm: () -> Unit,
  onLaunchWhatsAppDirect: (Context) -> Unit,
  onSimulateSuccessfulDispatch: () -> Unit,
  generatedWaText: String,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkCanvasBase)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Overview Metrics Banner (3 columns)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Metric 1: Aktif Shift
      Column(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(12.dp))
          .background(DarkSurfaceContainerHigh)
          .padding(12.dp)
      ) {
        Text(
          text = "AKTIF SHIFT",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = state.activeShift.label,
          color = AmberPrimary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(6.dp).background(EmeraldSecondary, CircleShape))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = state.activeShift.scheduleTime,
            color = EmeraldSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Metric 2: Total Pax
      Column(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(12.dp))
          .background(DarkSurfaceContainerHigh)
          .padding(12.dp)
      ) {
        Text(
          text = "TOTAL PAX",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
          Text(
            text = "${state.totalPax}",
            color = TextOnSurface,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "Box",
            color = TextOnSurfaceVariant,
            fontSize = 11.sp
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${state.totalCuti} Cuti/Off-Site",
          color = CrimsonTertiary,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold,
          maxLines = 1
        )
      }

      // Metric 3: Status Log
      Column(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(12.dp))
          .background(DarkSurfaceContainerHigh)
          .padding(12.dp)
      ) {
        Text(
          text = "STATUS LOG",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Siap",
          color = EmeraldSecondary,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "WA Template",
          color = TextOnSurfaceVariant,
          fontSize = 11.sp
        )
      }
    }

    // 2. Fast Dispatch Generator Controls Card
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceContainer)
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(34.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(AmberPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Bolt,
              contentDescription = null,
              tint = AmberPrimary,
              modifier = Modifier.size(20.dp)
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Fast Dispatch Generator",
              color = TextOnSurface,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Otomatisasi pesan rekap drop zone catering",
              color = TextOnSurfaceVariant,
              fontSize = 11.sp
            )
          }
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(DarkSurfaceBright)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "01 Okt 2026",
            color = TextOnSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Shift Buttons
      Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
          text = "PILIH SHIFT KERJA",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceContainerLowest)
            .padding(4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          ShiftType.values().forEach { shift ->
            val isSelected = shift == state.activeShift
            val paxCount = when (shift) {
              ShiftType.PAGI -> 35
              ShiftType.SIANG -> state.totalPax
              ShiftType.MALAM -> 37
            }
            Column(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) AmberPrimary else Color.Transparent)
                .clickable { onShiftSelect(shift) }
                .padding(vertical = 8.dp)
                .testTag("shift_btn_${shift.label.lowercase()}"),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = shift.label,
                color = if (isSelected) Color(0xFF472A00) else TextOnSurfaceVariant,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "$paxCount Box",
                color = if (isSelected) Color(0xFF472A00).copy(alpha = 0.8f) else TextOnSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 10.sp
              )
            }
          }
        }
      }

      // Vendor Selector Row
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Storefront,
              contentDescription = null,
              tint = AmberPrimary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "KONTRAK VENDOR CATERING TAMBANG",
              color = TextOnSurfaceVariant,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(EmeraldSecondary.copy(alpha = 0.12f))
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = EmeraldSecondary,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = state.activeVendor.status,
                color = EmeraldSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Vendor Pills
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          state.allVendors.forEach { vendor ->
            val isSelected = vendor.id == state.activeVendor.id
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) AmberPrimary else DarkSurfaceContainerHigh)
                .clickable { onVendorSelect(vendor.id) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .testTag("vendor_pill_${vendor.id}")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF472A00),
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                  text = vendor.name.replace("PT ", "").replace("CV ", ""),
                  color = if (isSelected) Color(0xFF472A00) else TextOnSurfaceVariant,
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
              }
            }
          }

          // Kelola Vendor pill button
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(DarkSurfaceBright)
              .border(1.dp, AmberPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
              .clickable(onClick = onOpenVendorEdit)
              .padding(horizontal = 10.dp, vertical = 6.dp)
              .testTag("btn_kelola_vendor")
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = AmberPrimary,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Kelola",
                color = AmberPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Active Vendor Details Card
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceContainerHigh)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .padding(12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(AmberPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.LocalDining,
                  contentDescription = null,
                  tint = AmberPrimary,
                  modifier = Modifier.size(22.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = state.activeVendor.name,
                    color = TextOnSurface,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(4.dp))
                      .background(DarkSurfaceBright)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = state.activeVendor.status,
                      color = AmberPrimary,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = EmeraldSecondary,
                    modifier = Modifier.size(13.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${state.activeVendor.phone} (${state.activeVendor.pic})",
                    color = EmeraldSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
              }
            }

            IconButton(
              onClick = onOpenVendorEdit,
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceBright)
            ) {
              Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Vendor",
                tint = AmberPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Quick Add Special Notes Chips
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
          text = "TAMBAH CATATAN TAMBAHAN (KLIK UNTUK PASANG)",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 0.5.sp
        )
        val chips = listOf(
          "Harap tepat waktu maksimal 15 mnt sebelum shift",
          "Pisahkan sambal & kuah pada wadah tertutup terpisah",
          "Sediakan paket sendok garpu & tisu ekstra",
          "Driver wajib konfirmasi jam tiba di tiap titik drop mes & pit",
          "Driver hubungi pengawas tiap tiba di lokasi mes"
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          chips.forEachIndexed { idx, note ->
            val isSelected = state.quickNotes.contains(note)
            val shortLabel = when (idx) {
              0 -> "Harap tepat waktu"
              1 -> "Pisahkan sambal"
              2 -> "Sediakan sendok ekstra"
              3 -> "+ Konfirmasi Jam Tiba Drop"
              else -> "+ Hubungi Pengawas Mes"
            }
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) AmberPrimaryContainer else DarkSurfaceContainerHigh)
                .clickable { onToggleNote(note) }
                .padding(horizontal = 10.dp, vertical = 7.dp)
                .testTag("chip_note_$idx")
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                  contentDescription = null,
                  tint = if (isSelected) Color(0xFF472A00) else AmberPrimary,
                  modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = shortLabel,
                  color = if (isSelected) Color(0xFF472A00) else TextOnSurface,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }
        }
      }
    }

    // 3. Route & ETA Banner
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceContainerHigh)
        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.AltRoute,
            contentDescription = null,
            tint = AmberPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "ESTIMASI RUTE & WAKTU TEMPUH (ETA)",
            color = AmberPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(EmeraldSecondary.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(
            text = "~58 mnt • 15.6 km",
            color = EmeraldSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.LocalShipping,
          contentDescription = null,
          tint = TextOnSurfaceVariant,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Rute: Dapur ${state.activeVendor.name} → Mes 1-4 → Site Office → Pit North Front",
          color = TextOnSurfaceVariant,
          fontSize = 11.sp,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Armada: LV Pit-04 (4WD Terisolasi)",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp
        )
        Text(
          text = "Driver: Bpk. Rustam (+62 821-4401-9981)",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }

    // 4. Watermark Logo Preview Controls
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceContainerHigh)
        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = null,
            tint = AmberPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Atur Watermark Logo Preview",
            color = TextOnSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = "Scale: ${state.watermarkConfig.scaleLabel} • Opacity: ${state.watermarkConfig.opacityLabel}",
          color = EmeraldSecondary,
          fontSize = 10.sp,
          fontWeight = FontWeight.Medium
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Scale Buttons
        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.PhotoSizeSelectSmall,
                contentDescription = null,
                tint = TextOnSurfaceVariant,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Scale",
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
            Text(
              text = state.watermarkConfig.scaleLabel,
              color = AmberPrimary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val scales = listOf(0.6f to "60%", 0.8f to "80%", 1.0f to "100%", 1.3f to "130%")
            scales.forEach { (sc, lbl) ->
              val isSelected = (lbl == state.watermarkConfig.scaleLabel)
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(30.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (isSelected) AmberPrimary else DarkSurfaceContainer)
                  .clickable { onSetWatermarkScale(sc, lbl) },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = lbl,
                  color = if (isSelected) Color(0xFF472A00) else TextOnSurfaceVariant,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        // Opacity Buttons
        Column(modifier = Modifier.weight(1f)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Opacity,
                contentDescription = null,
                tint = TextOnSurfaceVariant,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Transparansi",
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
            Text(
              text = state.watermarkConfig.opacityLabel,
              color = EmeraldSecondary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            val opacities = listOf(
              0.05f to "5%",
              0.12f to "12%",
              0.20f to "20%",
              0.30f to "30%"
            )
            opacities.forEach { (op, lbl) ->
              val isSelected = (lbl == state.watermarkConfig.opacityLabel.split(" ")[0])
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(30.dp)
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (isSelected) EmeraldSecondary else DarkSurfaceContainer)
                  .clickable { onSetWatermarkOpacity(op, "$lbl Standar") },
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = lbl,
                  color = if (isSelected) Color(0xFF003824) else TextOnSurfaceVariant,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    }

    // 5. WhatsApp Chat Bubble Preview
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceContainerLowest)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(8.dp).background(EmeraldSecondary, CircleShape))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Live WhatsApp Message Preview",
            color = TextOnSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
        Text(
          text = "Format Otomatis",
          color = TextOnSurfaceVariant,
          fontSize = 10.sp
        )
      }

      // Authentic WhatsApp Bubble with Mucoindo Watermark
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(WaDarkGreen)
          .padding(14.dp)
      ) {
        // Watermark in background
        Box(
          modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
            .graphicsLayer(
              scaleX = state.watermarkConfig.scale,
              scaleY = state.watermarkConfig.scale,
              alpha = state.watermarkConfig.opacity
            ),
          contentAlignment = Alignment.Center
        ) {
          MucoindoLogo(
            modifier = Modifier.size(160.dp),
            tintColor = Color.White
          )
        }

        // WhatsApp message content
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Bubble header
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "To: ${state.activeVendor.name}",
              color = Color(0xFF80CBC4),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.weight(1f, fill = false)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color.White.copy(alpha = 0.1f))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "MIP PIT NORTH",
                  color = EmeraldSecondary,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = state.activeShift.scheduleTime,
                color = Color(0xFF80CBC4),
                fontSize = 10.sp
              )
            }
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(Color(0xFF80CBC4).copy(alpha = 0.2f))
          )

          // Preformatted Message Text
          Text(
            text = generatedWaText,
            color = Color(0xFFE9FBF7),
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            lineHeight = 18.sp
          )

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(1.dp)
              .background(Color(0xFF80CBC4).copy(alpha = 0.2f))
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = EmeraldSecondary,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Rekap Resmi PT. Mucoindo Prakasa",
                color = Color(0xFF80CBC4),
                fontSize = 10.sp
              )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Auto-Gen Template",
                color = Color(0xFF80CBC4),
                fontSize = 10.sp
              )
              Spacer(modifier = Modifier.width(4.dp))
              Icon(
                imageVector = Icons.Default.DoneAll,
                contentDescription = null,
                tint = EmeraldSecondary,
                modifier = Modifier.size(14.dp)
              )
            }
          }
        }
      }
    }

    // 6. Action Buttons: Salin Teks Rekap & Kirim WhatsApp
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(6.dp).background(EmeraldSecondary, CircleShape))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Status Aksi Siap Eksekusi",
            color = TextOnSurfaceVariant,
            fontSize = 11.sp
          )
        }
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(AmberPrimary.copy(alpha = 0.2f))
            .border(1.dp, AmberPrimary.copy(alpha = 0.4f), CircleShape)
            .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.TouchApp,
              contentDescription = null,
              tint = AmberPrimary,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Klik untuk Uji Coba",
              color = AmberPrimary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = { onCopyRekap(context) },
          colors = ButtonDefaults.buttonColors(
            containerColor = DarkSurfaceContainerHighest,
            contentColor = TextOnSurface
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
            .testTag("btn_salin_rekap")
        ) {
          Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = null,
            tint = AmberPrimary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Salin Teks Rekap",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Button(
          onClick = onOpenWaConfirm,
          colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldSecondary,
            contentColor = Color(0xFF003824)
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("btn_kirim_whatsapp")
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            tint = Color(0xFF003824),
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Kirim WhatsApp",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // 7. Dispatch History Log Card
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceContainer)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = TextOnSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Dispatch History Log",
            color = TextOnSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = "Hari Ini (01 Okt)",
          color = TextOnSurfaceVariant,
          fontSize = 11.sp
        )
      }

      state.dispatchLogs.forEach { log ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceContainerHigh)
            .padding(12.dp),
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
                .clip(RoundedCornerShape(8.dp))
                .background(
                  if (log.isConfirmed) EmeraldSecondary.copy(alpha = 0.2f)
                  else AmberPrimary.copy(alpha = 0.2f)
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = if (log.isConfirmed) Icons.Default.CheckCircle else Icons.Default.EditNote,
                contentDescription = null,
                tint = if (log.isConfirmed) EmeraldSecondary else AmberPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = log.title,
                color = TextOnSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = log.desc,
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
          }

          if (!log.isConfirmed) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(AmberPrimary)
                .clickable(onClick = onOpenWaConfirm)
                .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(
                text = "Kirim",
                color = Color(0xFF472A00),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          } else {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(EmeraldSecondary.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text(
                text = log.status,
                color = EmeraldSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // 8. Field Operation Checklist Tip
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(DarkSurfaceContainerLow)
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(EmeraldSecondaryContainer),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = null,
          tint = Color(0xFF003824),
          modifier = Modifier.size(16.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Text(
        text = "Perubahan data cuti karyawan terintegrasi langsung dengan modul Karyawan Off-Site. Rekap otomatis menghitung pengurangan box secara live.",
        color = TextOnSurfaceVariant,
        fontSize = 11.sp,
        lineHeight = 16.sp
      )
    }

    Spacer(modifier = Modifier.height(30.dp))
  }

  // --- Modals ---
  // Modal 1: Edit Vendor Modal
  if (state.isVendorEditModalOpen) {
    VendorEditBottomSheet(
      vendor = state.activeVendor,
      onDismiss = onCloseVendorEdit,
      onSave = onSaveVendor
    )
  }

  // Modal 2: Confirm WhatsApp Modal
  if (state.isWaConfirmModalOpen) {
    WhatsAppConfirmBottomSheet(
      vendor = state.activeVendor,
      messageText = generatedWaText,
      onDismiss = onCloseWaConfirm,
      onLaunchApp = { onLaunchWhatsAppDirect(context) },
      onSimulateSent = onSimulateSuccessfulDispatch
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorEditBottomSheet(
  vendor: Vendor,
  onDismiss: () -> Unit,
  onSave: (Vendor) -> Unit
) {
  var name by remember { mutableStateOf(vendor.name) }
  var phone by remember { mutableStateOf(vendor.phone) }
  var pic by remember { mutableStateOf(vendor.pic) }
  var status by remember { mutableStateOf(vendor.status) }
  var notes by remember { mutableStateOf(vendor.specialNote) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = DarkSurfaceContainerHigh
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Storefront,
            contentDescription = null,
            tint = AmberPrimary,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = "Edit Data Vendor Catering",
              color = TextOnSurface,
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Kelola kontak, status kontrak, dan instruksi khusus",
              color = TextOnSurfaceVariant,
              fontSize = 11.sp
            )
          }
        }
      }

      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Nama Vendor Catering") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = AmberPrimary,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = TextOnSurface,
          unfocusedTextColor = TextOnSurface
        )
      )

      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Nomor WhatsApp / Telepon Vendor") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = AmberPrimary,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = TextOnSurface,
          unfocusedTextColor = TextOnSurface
        )
      )

      OutlinedTextField(
        value = pic,
        onValueChange = { pic = it },
        label = { Text("Nama Kontak Person / PIC") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = AmberPrimary,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = TextOnSurface,
          unfocusedTextColor = TextOnSurface
        )
      )

      Text(
        text = "Status Kontrak Vendor",
        color = TextOnSurfaceVariant,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("Kontrak Utama Aktif", "Vendor Cadangan", "Trial").forEach { st ->
          val isSel = (status == st)
          Box(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSel) AmberPrimary.copy(alpha = 0.2f) else DarkSurfaceContainer)
              .border(
                1.dp,
                if (isSel) AmberPrimary else BorderSubtle,
                RoundedCornerShape(8.dp)
              )
              .clickable { status = st }
              .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = st.replace("Vendor ", "").replace("Kontrak ", ""),
              color = if (isSel) AmberPrimary else TextOnSurfaceVariant,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      OutlinedTextField(
        value = notes,
        onValueChange = { notes = it },
        label = { Text("Catatan Khusus Pengiriman") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 2,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = AmberPrimary,
          unfocusedBorderColor = BorderSubtle,
          focusedTextColor = TextOnSurface,
          unfocusedTextColor = TextOnSurface
        )
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 10.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(
            containerColor = DarkSurfaceContainerHighest,
            contentColor = TextOnSurface
          ),
          modifier = Modifier.weight(1f).height(44.dp)
        ) {
          Text("Batal")
        }
        Button(
          onClick = {
            onSave(
              vendor.copy(
                name = name.trim(),
                phone = phone.trim(),
                pic = pic.trim(),
                status = status,
                specialNote = notes.trim()
              )
            )
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = AmberPrimary,
            contentColor = Color(0xFF472A00)
          ),
          modifier = Modifier.weight(1f).height(44.dp)
        ) {
          Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsAppConfirmBottomSheet(
  vendor: Vendor,
  messageText: String,
  onDismiss: () -> Unit,
  onLaunchApp: () -> Unit,
  onSimulateSent: () -> Unit
) {
  val cleanPhone = vendor.phone.replace(Regex("[^0-9]"), "").let {
    if (it.startsWith("0")) "62" + it.substring(1) else it
  }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor = DarkSurfaceContainerHigh
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(WaDarkGreen),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            tint = EmeraldSecondary,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "Konfirmasi Kirim WhatsApp",
            color = TextOnSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${vendor.name} (${vendor.phone})",
            color = EmeraldSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }

      // Preview box
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(DarkSurfaceContainerLowest)
          .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
          .padding(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Preview Pesan Siap Kirim",
            color = TextOnSurfaceVariant,
            fontSize = 10.sp
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).background(EmeraldSecondary, CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Format Sesuai",
              color = EmeraldSecondary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = messageText,
          color = Color(0xFFE9FBF7),
          fontSize = 11.sp,
          lineHeight = 16.sp,
          maxLines = 8,
          overflow = TextOverflow.Ellipsis
        )
      }

      Text(
        text = "Link tujuan: https://wa.me/$cleanPhone",
        color = EmeraldSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold
      )

      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Button(
          onClick = onLaunchApp,
          colors = ButtonDefaults.buttonColors(
            containerColor = EmeraldSecondary,
            contentColor = Color(0xFF003824)
          ),
          modifier = Modifier.fillMaxWidth().height(48.dp),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Buka Aplikasi WhatsApp Sekarang", fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = onSimulateSent,
          colors = ButtonDefaults.buttonColors(
            containerColor = AmberPrimary,
            contentColor = Color(0xFF472A00)
          ),
          modifier = Modifier.fillMaxWidth().height(44.dp),
          shape = RoundedCornerShape(10.dp)
        ) {
          Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Simulasi Terkirim & Perbarui Status Log", fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(
            containerColor = DarkSurfaceContainerHighest,
            contentColor = TextOnSurfaceVariant
          ),
          modifier = Modifier.fillMaxWidth().height(40.dp),
          shape = RoundedCornerShape(10.dp)
        ) {
          Text("Batal")
        }
      }
    }
  }
}
