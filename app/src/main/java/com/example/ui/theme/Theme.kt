package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LollipopColorScheme =
  lightColorScheme(
    primary = LollipopTeal,
    onPrimary = Color.White,
    primaryContainer = LollipopTealDark,
    onPrimaryContainer = Color.White,
    secondary = LollipopPink,
    onSecondary = Color.White,
    secondaryContainer = LollipopPinkDark,
    onSecondaryContainer = Color.White,
    background = LollipopBackground,
    onBackground = LollipopTextPrimary,
    surface = LollipopSurface,
    onSurface = LollipopTextPrimary,
    surfaceVariant = LollipopBackground,
    onSurfaceVariant = LollipopTextSecondary
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Disable dynamic colors to preserve Lollipop aesthetic
  content: @Composable () -> Unit,
) {
  // Always use the classic Lollipop theme to ensure retro styling!
  val colorScheme = LollipopColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
