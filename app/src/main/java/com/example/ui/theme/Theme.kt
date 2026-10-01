package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
  primary = AmberPrimary,
  onPrimary = OnAmberPrimary,
  primaryContainer = AmberPrimaryContainer,
  onPrimaryContainer = DarkCanvasBase,
  secondary = EmeraldSecondary,
  onSecondary = OnEmeraldSecondary,
  secondaryContainer = EmeraldSecondaryContainer,
  onSecondaryContainer = DarkCanvasBase,
  tertiary = CrimsonTertiary,
  onTertiary = CrimsonErrorContainer,
  tertiaryContainer = CrimsonTertiaryContainer,
  background = DarkCanvasBase,
  onBackground = TextOnSurface,
  surface = DarkCanvasBase,
  onSurface = TextOnSurface,
  surfaceVariant = DarkSurfaceContainerHighest,
  onSurfaceVariant = TextOnSurfaceVariant,
  surfaceContainer = DarkSurfaceContainer,
  surfaceContainerHigh = DarkSurfaceContainerHigh,
  surfaceContainerHighest = DarkSurfaceContainerHighest,
  surfaceContainerLow = DarkSurfaceContainerLow,
  surfaceContainerLowest = DarkSurfaceContainerLowest,
  surfaceBright = DarkSurfaceBright,
  outline = BorderSubtle,
  outlineVariant = BorderOutlineVariant,
  error = CrimsonError,
  onError = DarkCanvasBase
)

private val LightColorScheme = DarkColorScheme // Default to high-contrast dark industrial theme

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      if (window != null) {
        window.statusBarColor = DarkCanvasBase.toArgb()
        window.navigationBarColor = DarkCanvasBase.toArgb()
        val controller = WindowCompat.getInsetsController(window, view)
        controller.isAppearanceLightStatusBars = false
        controller.isAppearanceLightNavigationBars = false
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
