package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShiftType
import com.example.ui.components.DigitalSignatureBox
import com.example.ui.components.MucoindoLogo
import com.example.ui.theme.AmberPrimary
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.DarkSurfaceBright
import com.example.ui.theme.DarkSurfaceContainer
import com.example.ui.theme.DarkSurfaceContainerHigh
import com.example.ui.theme.DarkSurfaceContainerHighest
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.EmeraldSecondaryContainer
import com.example.ui.theme.PaperBackground
import com.example.ui.theme.PaperBorder
import com.example.ui.theme.PaperSubtle
import com.example.ui.theme.PaperTextPrimary
import com.example.ui.theme.PaperTextSecondary
import com.example.ui.theme.TextOnSurface
import com.example.ui.theme.TextOnSurfaceVariant
import com.example.ui.viewmodel.CateringUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlipBeritaAcaraScreen(
  state: CateringUiState,
  onDownloadPdf: () -> Unit,
  onSendWaManifest: () -> Unit,
  onShareChannelSelect: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val scrollState = rememberScrollState()
  var watermarkOpacity by remember { mutableStateOf(0.12f) }
  var isShareSheetOpen by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(DarkCanvasBase)
      .verticalScroll(scrollState)
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. Top Action & Meta Bar
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
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(EmeraldSecondary.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "BERITA ACARA RESMI",
                color = EmeraldSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "Shift ${state.activeShift.label}",
              color = TextOnSurfaceVariant,
              fontSize = 10.sp
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Slip Serah Terima Catering",
            color = TextOnSurface,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "BA-CAT/MIP/2026/10/01-S02",
            color = TextOnSurfaceVariant,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(DarkSurfaceBright)
              .clickable { onDownloadPdf() }
              .testTag("btn_print_slip"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Print,
              contentDescription = "Print Slip",
              tint = TextOnSurface,
              modifier = Modifier.size(19.dp)
            )
          }

          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(DarkSurfaceBright)
              .clickable { isShareSheetOpen = true }
              .testTag("btn_share_slip"),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share Document",
              tint = EmeraldSecondary,
              modifier = Modifier.size(19.dp)
            )
          }
        }
      }

      // Quick Control Presets (Watermark Opacity & Siap Cetak)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(DarkSurfaceContainerHighest.copy(alpha = 0.5f))
          .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = AmberPrimary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Watermark:",
            color = TextOnSurfaceVariant,
            fontSize = 11.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(0.08f to "8%", 0.12f to "12%", 0.20f to "20%").forEach { (op, label) ->
              val isSel = (watermarkOpacity == op)
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (isSel) AmberPrimary else DarkSurfaceBright)
                  .clickable { watermarkOpacity = op }
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = label,
                  color = if (isSel) Color(0xFF472A00) else TextOnSurfaceVariant,
                  fontSize = 10.sp,
                  fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                )
              }
            }
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(6.dp).background(EmeraldSecondary, CircleShape))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "SIAP CETAK",
            color = EmeraldSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    // 2. Paper View / Document Canvas (Clean white-slate paper with official branding)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(PaperBackground)
        .border(1.dp, PaperBorder, RoundedCornerShape(14.dp))
        .padding(16.dp)
    ) {
      // Watermark in background
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(380.dp)
          .align(Alignment.Center)
          .graphicsLayer(alpha = watermarkOpacity),
        contentAlignment = Alignment.Center
      ) {
        MucoindoLogo(
          modifier = Modifier.size(260.dp),
          tintColor = Color.DarkGray
        )
      }

      // Document Foreground Content
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Document Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            MucoindoLogo(modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "PT. MUCOINDO PRAKASA",
                color = PaperTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.2).sp
              )
              Text(
                text = "DIVISI LOGISTIK & MESS PIT NORTH",
                color = PaperTextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
            }
          }

          Column(horizontalAlignment = Alignment.End) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE2E8F0))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "SOP-LOG-CAT-04",
                color = Color(0xFF1E293B),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "Rev. 03 / ISO 9001",
              color = Color(0xFF64748B),
              fontSize = 8.sp,
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }

        // 4-Item Metadata Box (Nomor BA, Tanggal & Jam, Penyedia, Armada & Driver)
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(PaperSubtle)
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "NOMOR BERITA ACARA",
                color = Color(0xFF64748B),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "BA-CAT/MIP/2026/10/01-S02",
                color = PaperTextPrimary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "TANGGAL & JAM TIBA",
                color = Color(0xFF64748B),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "01 Okt 2026 • 11:15 WITA",
                color = PaperTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "PENYEDIA CATERING",
                color = Color(0xFF64748B),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = state.activeVendor.name,
                color = PaperTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "ARMADA & DRIVER",
                color = Color(0xFF64748B),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Bpk. Rustam (LV Pit-04)",
                color = PaperTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Temperature & Safety Verification Strip (QC HSE Lolos)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFE6FFFA))
            .border(1.dp, Color(0xFFA7F3D0), RoundedCornerShape(8.dp))
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Thermostat,
              contentDescription = null,
              tint = Color(0xFF059669),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "INSPEKSI SUHU BOX",
                color = Color(0xFF065F46),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "68°C • Insulated Container Optimal",
                color = Color(0xFF064E3B),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFF10B981))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(13.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "QC HSE Lolos",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Rincian Alokasi Drop Zone Table
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, PaperBorder, RoundedCornerShape(8.dp))
        ) {
          // Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(PaperSubtle)
              .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "DROP LOKASI / MES",
              color = Color(0xFF334155),
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1.8f)
            )
            Text(
              text = "JUMLAH",
              color = Color(0xFF334155),
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1f)
            )
            Text(
              text = "VERIFIKASI",
              color = Color(0xFF334155),
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1f)
            )
          }

          // Table Rows
          val rows = listOf(
            Triple("Mes 1", "Pit North Camp Block A", "${state.mes1Pax} Pax"),
            Triple("Mes 2", "Pit North Camp Block B", "${state.mes2Pax} Pax"),
            Triple(
              "Mes 3",
              if (state.mes3CutiNames.isNotEmpty())
                "${state.mes3CutiNames.size} Cuti: ${state.mes3CutiNames.joinToString(", ")} dikecualikan"
              else "Normal roster",
              "${state.mes3Pax} Pax"
            ),
            Triple("Mes 4", "Camp Supervisor & Safety", "${state.mes4Pax} Pax"),
            Triple("Site Office & Pit North", "Main Workshop & Control Tower", "${state.siteOfficePax} Pax")
          )

          rows.forEachIndexed { i, (loc, sub, pax) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(if (i % 2 == 0) Color.White else PaperBackground)
                .padding(horizontal = 10.dp, vertical = 7.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1.8f)) {
                Text(
                  text = loc,
                  color = PaperTextPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = sub,
                  color = if (loc == "Mes 3" && state.mes3CutiNames.isNotEmpty()) Color(0xFFD97706) else Color(0xFF64748B),
                  fontSize = 9.sp,
                  fontWeight = if (loc == "Mes 3" && state.mes3CutiNames.isNotEmpty()) FontWeight.SemiBold else FontWeight.Normal
                )
              }
              Text(
                text = pax,
                color = PaperTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
              )
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color(0xFF059669),
                  modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                  text = "Lengkap",
                  color = Color(0xFF059669),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          // Table Footer: Total Diserahkan
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(PaperSubtle)
              .border(1.dp, Color(0xFFCBD5E1))
              .padding(horizontal = 10.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "TOTAL DISERAHKAN",
              color = PaperTextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.weight(1.8f)
            )
            Text(
              text = "${state.totalPax} Box",
              color = PaperTextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.weight(1f)
            )
            Text(
              text = "100% SESUAI ROSTER",
              color = Color(0xFF059669),
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.weight(1f)
            )
          }
        }

        // Menu Utama Box
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(PaperBackground)
            .border(1.dp, PaperBorder, RoundedCornerShape(8.dp))
            .padding(8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.RestaurantMenu,
              contentDescription = null,
              tint = Color(0xFF0284C7),
              modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Menu: Nasi Putih + Ayam Bakar Madu + Tumis Buncis + Buah Semangka",
              color = PaperTextSecondary,
              fontSize = 10.sp,
              maxLines = 1
            )
          }
          Text(
            text = "Batch #02-SNG",
            color = Color(0xFF64748B),
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
          )
        }

        // Dual Digital Signatures (Bpk. Rustam & Pak Tina)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Pihak 1: Driver
          Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "PIHAK 1 (PENYEDIA / DRIVER)",
              color = Color(0xFF64748B),
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            DigitalSignatureBox(isReceiver = false, timestampOrId = "11:17:04 WITA")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Bpk. Rustam",
              color = PaperTextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Driver PT Berkah Boga",
              color = PaperTextSecondary,
              fontSize = 9.sp
            )
          }

          // Pihak 2: Penerima MIP
          Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "PIHAK 2 (PENERIMA MESS / MIP)",
              color = Color(0xFF64748B),
              fontSize = 8.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            DigitalSignatureBox(isReceiver = true, timestampOrId = "ID: MIP-9902")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Pak Tina / Pengawas",
              color = PaperTextPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Logistik PT Mucoindo Prakasa",
              color = PaperTextSecondary,
              fontSize = 9.sp
            )
          }
        }

        // Verification Stamp & Mini QR Code Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(PaperBackground)
            .border(1.dp, PaperBorder, RoundedCornerShape(8.dp))
            .padding(8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            // Mini QR box
            Box(
              modifier = Modifier
                .size(30.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(4.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "QR",
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1E293B)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "SECURE CRYPTO HASH: #BA-MIP-8841-A9",
                color = PaperTextPrimary,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Tervalidasi di Server Intranet MIP Pit North",
                color = Color(0xFF64748B),
                fontSize = 8.sp
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFFDBEAFE))
              .padding(horizontal = 6.dp, vertical = 3.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = Color(0xFF1D4ED8),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(3.dp))
              Text(
                text = "ASLI / VALID",
                color = Color(0xFF1D4ED8),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // 3. Quick Action Hub below Paper View (Download PDF & Kirim WA Manifest)
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        onClick = onDownloadPdf,
        colors = ButtonDefaults.buttonColors(
          containerColor = AmberPrimary,
          contentColor = Color(0xFF472A00)
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.weight(1f).height(48.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Download,
          contentDescription = null,
          modifier = Modifier.size(19.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Download PDF", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      Button(
        onClick = onSendWaManifest,
        colors = ButtonDefaults.buttonColors(
          containerColor = EmeraldSecondaryContainer,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.weight(1f).height(48.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = null,
          modifier = Modifier.size(19.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text("Kirim WA Manifest", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    // 4. Log Aktivitas Berita Acara
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(DarkSurfaceContainer)
        .padding(14.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
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
            tint = AmberPrimary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Log Aktivitas Berita Acara",
            color = TextOnSurface,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = "Realtime Synced",
          color = EmeraldSecondary,
          fontSize = 10.sp,
          fontFamily = FontFamily.Monospace
        )
      }

      val activityLogs = listOf(
        "11:15" to "Kedatangan 54 Box dikonfirmasi oleh Petugas Gerbang Pos 1.",
        "11:17" to "Tanda tangan serah terima digital diverifikasi oleh Bpk. Rustam & Pak Tina.",
        "11:18" to "Slip PDF BA-CAT/MIP/2026/10/01-S02 berhasil di-generate secara otomatis."
      )

      activityLogs.forEach { (time, desc) ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.Top
        ) {
          Text(
            text = time,
            color = AmberPrimary,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(42.dp)
          )
          Text(
            text = desc,
            color = TextOnSurfaceVariant,
            fontSize = 11.sp,
            lineHeight = 16.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }

  // Share & Export Document Bottom Sheet
  if (isShareSheetOpen) {
    ShareDocumentBottomSheet(
      onDismiss = { isShareSheetOpen = false },
      onChannelSelect = { channel ->
        isShareSheetOpen = false
        onShareChannelSelect(channel)
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDocumentBottomSheet(
  onDismiss: () -> Unit,
  onChannelSelect: (String) -> Unit
) {
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
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = null,
          tint = AmberPrimary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Distribusi Dokumen Berita Acara",
          color = TextOnSurface,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Text(
        text = "Pilih saluran untuk mengirimkan salinan digital Berita Acara Serah Terima ini kepada pihak berkepentingan:",
        color = TextOnSurfaceVariant,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )

      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Option 1
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceContainer)
            .clickable { onChannelSelect("WA Group Logistik Pit North") }
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
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EmeraldSecondaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "WA Group Logistik & Mess",
                color = TextOnSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Kirim format ringkas + tautan PDF slip",
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextOnSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }

        // Option 2
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceContainer)
            .clickable { onChannelSelect("WhatsApp Vendor Catering") }
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
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AmberPrimary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = AmberPrimary,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "PT Berkah Boga Mandiri",
                color = TextOnSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Konfirmasi penerimaan tanda bukti vendor",
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextOnSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }

        // Option 3
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurfaceContainer)
            .clickable { onChannelSelect("Site HSE & Audit Storage") }
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
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DarkSurfaceBright),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = TextOnSurface,
                modifier = Modifier.size(18.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Arsip HSE & ERP Mucoindo",
                color = TextOnSurface,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Simpan salinan untuk audit higienitas bulanan",
                color = TextOnSurfaceVariant,
                fontSize = 10.sp
              )
            }
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextOnSurfaceVariant,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(
          containerColor = DarkSurfaceContainerHighest,
          contentColor = TextOnSurface
        ),
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 6.dp, bottom = 24.dp)
          .height(44.dp)
      ) {
        Text("Tutup")
      }
    }
  }
}
