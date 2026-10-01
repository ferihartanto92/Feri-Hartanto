package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppScreen
import com.example.ui.components.AppTopBar
import com.example.ui.components.BottomNavBar
import com.example.ui.components.ShiftZoneDrawer
import com.example.ui.screens.KaryawanScreen
import com.example.data.local.AppDatabase
import com.example.data.repository.CateringRepository
import com.example.ui.screens.MonitoringScreen
import com.example.ui.screens.SlipBeritaAcaraScreen
import com.example.ui.screens.WhatsappDispatchScreen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCanvasBase
import com.example.ui.theme.DarkSurfaceContainerHighest
import com.example.ui.theme.EmeraldSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextOnSurface
import com.example.ui.viewmodel.CateringViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: CateringViewModel by viewModels {
    object : androidx.lifecycle.ViewModelProvider.Factory {
      @Suppress("UNCHECKED_CAST")
      override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getInstance(applicationContext)
        val repository = CateringRepository(database)
        return CateringViewModel(repository) as T
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = true) {
        MainAppContent(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun MainAppContent(viewModel: CateringViewModel) {
  val context = LocalContext.current
  val scope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsState()

  var toastMessage by remember { mutableStateOf<String?>(null) }

  // Listen to toast events from ViewModel
  LaunchedEffect(Unit) {
    viewModel.toastEvent.collectLatest { msg ->
      toastMessage = msg
      delay(2600)
      if (toastMessage == msg) {
        toastMessage = null
      }
    }
  }

  // Back handler
  BackHandler(enabled = uiState.isSideDrawerOpen || uiState.currentScreen != AppScreen.DISPATCH) {
    if (uiState.isSideDrawerOpen) {
      viewModel.closeSideDrawer()
    } else {
      viewModel.setScreen(AppScreen.DISPATCH)
    }
  }

  Box(modifier = Modifier.fillMaxSize().background(DarkCanvasBase)) {
    Scaffold(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding(),
      containerColor = DarkCanvasBase,
      topBar = {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          AppTopBar(
            modifier = Modifier.widthIn(max = 680.dp),
            onOpenDrawer = { viewModel.openSideDrawer() },
            onFastBroadcastClick = {
              viewModel.setScreen(AppScreen.DISPATCH)
              viewModel.openWaConfirmModal()
            },
            onNotificationClick = {
              scope.launch {
                toastMessage = "3 Notifikasi: Jadwal shift siang diperbarui"
                delay(2600)
                toastMessage = null
              }
            }
          )
        }
      },
      bottomBar = {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          BottomNavBar(
            modifier = Modifier.widthIn(max = 680.dp),
            currentScreen = uiState.currentScreen,
            onScreenSelect = { viewModel.setScreen(it) },
            orderBadgeCount = uiState.totalPax
          )
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        contentAlignment = Alignment.TopCenter
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 680.dp)
        ) {
          when (uiState.currentScreen) {
          AppScreen.MONITORING -> {
            MonitoringScreen(
              state = uiState,
              onShiftSelect = { viewModel.setActiveShift(it) },
              onViewModeSelect = { viewModel.setMonitoringViewMode(it) },
              onToggleCuti = { viewModel.toggleEmployeeCuti(it) }
            )
          }

          AppScreen.ORDERS -> {
            SlipBeritaAcaraScreen(
              state = uiState,
              onDownloadPdf = { viewModel.simulateDownloadPdf() },
              onSendWaManifest = { viewModel.launchWhatsAppDirect(context) },
              onShareChannelSelect = { viewModel.shareDocumentChannel(it) }
            )
          }

          AppScreen.DISPATCH -> {
            WhatsappDispatchScreen(
              state = uiState,
              onShiftSelect = { viewModel.setActiveShift(it) },
              onVendorSelect = { viewModel.setActiveVendorId(it) },
              onOpenVendorEdit = { viewModel.openVendorEditModal() },
              onCloseVendorEdit = { viewModel.closeVendorEditModal() },
              onSaveVendor = { viewModel.saveVendor(it) },
              onToggleNote = { viewModel.toggleQuickNote(it) },
              onSetWatermarkScale = { sc, lbl -> viewModel.setWatermarkScale(sc, lbl) },
              onSetWatermarkOpacity = { op, lbl -> viewModel.setWatermarkOpacity(op, lbl) },
              onCopyRekap = { viewModel.copyRekapText(it) },
              onOpenWaConfirm = { viewModel.openWaConfirmModal() },
              onCloseWaConfirm = { viewModel.closeWaConfirmModal() },
              onLaunchWhatsAppDirect = { viewModel.launchWhatsAppDirect(it) },
              onSimulateSuccessfulDispatch = { viewModel.simulateSuccessfulDispatch() },
              generatedWaText = viewModel.generateWhatsAppMessage()
            )
          }

          AppScreen.KARYAWAN -> {
            KaryawanScreen(
              state = uiState,
              onToggleCuti = { viewModel.toggleEmployeeCuti(it) }
            )
          }
        }
        }
      }
    }

    // Right-side Slide-in Drawer for Shift & Zone Setup
    AnimatedVisibility(
      visible = uiState.isSideDrawerOpen,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.Black.copy(alpha = 0.65f))
          .clickable { viewModel.closeSideDrawer() }
      )
    }

    AnimatedVisibility(
      visible = uiState.isSideDrawerOpen,
      enter = slideInHorizontally(initialOffsetX = { it }),
      exit = slideOutHorizontally(targetOffsetX = { it }),
      modifier = Modifier.align(Alignment.CenterEnd)
    ) {
      ShiftZoneDrawer(
        activeShift = uiState.activeShift,
        selectedSector = uiState.selectedSector,
        onShiftSelect = {
          viewModel.setActiveShift(it)
          viewModel.closeSideDrawer()
        },
        onSectorSelect = {
          viewModel.setSelectedSector(it)
          viewModel.closeSideDrawer()
        },
        onClose = { viewModel.closeSideDrawer() },
        modifier = Modifier.statusBarsPadding()
      )
    }

    // Top Floating Toast (matching HTML mock: #toast)
    AnimatedVisibility(
      visible = toastMessage != null,
      enter = fadeIn(),
      exit = fadeOut(),
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 76.dp)
    ) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(10.dp))
          .background(DarkSurfaceContainerHighest)
          .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
          .padding(horizontal = 16.dp, vertical = 10.dp)
          .testTag("global_toast")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = EmeraldSecondary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = toastMessage ?: "",
            color = TextOnSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}
