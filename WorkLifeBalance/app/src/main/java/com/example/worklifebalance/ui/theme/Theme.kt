package com.example.worklifebalance.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = Color(0xFF1C1B1F), // Nền tổng thể của app (màn hình, Scaffold)
    surface = Color(0xFF2C2B2F), // Nền cho các bề mặt như Card, BottomSheet

    // Màu button
    primary = PastelBlue.copy(alpha = 0.8f), // Màu chính (nút, tiêu đề nổi bật)
    secondary = PastelGreen.copy(alpha = 0.8f), // Màu phụ (nút phụ, nhấn phụ)
    tertiary = PastelPurple.copy(alpha = 0.8f), // Màu bổ sung (icon, nhấn phụ)

    onPrimary = Color.Black, // Màu chữ/icon trên nền primary
    onSecondary = Color.Black, // Màu chữ/icon trên nền secondary
    onTertiary = Color.Black, // Màu chữ/icon trên nền tertiary

    primaryContainer = LightBlue, // Màu nền phụ cho các thành phần liên quan đến primary
    secondaryContainer = LightGreen, // Màu nền phụ cho các thành phần liên quan đến secondary
    tertiaryContainer = LightPurple, // Màu nền phụ cho các thành phần liên quan đến tertiary

    // Màu chữ/icon trên các nền phụ
    onPrimaryContainer = Blue, // Màu chữ/icon trên nền primaryContainer
    onSecondaryContainer = Green, // Màu chữ/icon trên nền secondaryContainer
    onTertiaryContainer = Purple, // Màu chữ/icon trên nền tertiaryContainer

    onBackground = Color(0xFFE6E1E5), // Màu chữ chính trên nền background
    onSurface = Color(0xFFE6E1E5), // Màu chữ chính trên nền surface
    onSurfaceVariant = Color.LightGray, // Màu chữ/icon phụ trên surface

    outline = Color.DarkGray, // Màu đường viền, border


)

private val LightColorScheme = lightColorScheme(
    background = Color(0xFFF8FAFC), // Nền tổng thể của app (màn hình, Scaffold)
    surface = Color.White,          // Nền cho các bề mặt như Card, BottomSheet

    // Màu button
    primary = PastelBlue, // Màu chính (nút, tiêu đề nổi bật)
    secondary = PastelGreen, // Màu phụ (nút phụ, nhấn phụ)
    tertiary = PastelPurple, // Màu bổ sung (icon, nhấn phụ)

    // Màu chữ/icon trên các nền chính
    onPrimary = Color.White, // Màu chữ/icon trên nền primary
    onSecondary = Color.Black, // Màu chữ/icon trên nền secondary
    onTertiary = Color.White, // Màu chữ/icon trên nền tertiary

    // Màu nền phụ
    primaryContainer = LightBlue, // Màu nền phụ cho các thành phần liên quan đến primary
    secondaryContainer = LightGreen, // Màu nền phụ cho các thành phần liên quan đến secondary
    tertiaryContainer = LightPurple, // Màu nền phụ cho các thành phần liên quan đến tertiary

    // Màu chữ/icon trên các nền phụ
    onPrimaryContainer = Blue, // Màu chữ/icon trên nền primaryContainer
    onSecondaryContainer = Green, // Màu chữ/icon trên nền secondaryContainer
    onTertiaryContainer = Purple, // Màu chữ/icon trên nền tertiaryContainer

    onBackground = Color(0xFF1C1B1F), // Màu chữ chính trên nền background
    onSurface = Color(0xFF1C1B1F),    // Màu chữ chính trên nền surface
    onSurfaceVariant = Color.Gray,    // Màu chữ/icon phụ trên surface

    outline = Color.LightGray.copy(alpha = 0.5f) // Màu đường viền, border
)

@Composable
fun WorkLifeBalanceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // true cho light theme, false cho dark theme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}