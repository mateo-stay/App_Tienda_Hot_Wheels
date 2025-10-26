package com.example.tiendahotwheels.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

private val HotRed = Color(0xFFFF1E00)
private val HotDarkRed = Color(0xFF8B0000)
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)
private val YellowAccent = Color(0xFFFFD700)
private val LightGray = Color(0xFFF5F5F5)
private val DarkColorScheme = darkColorScheme(
    primary = HotRed,
    secondary = HotDarkRed,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = White,
    onBackground = White,
    onSurface = White
)
private val LightColorScheme = lightColorScheme(
    primary = HotRed,
    secondary = YellowAccent,
    background = White,
    surface = LightGray,
    onPrimary = Black,
    onBackground = Black,
    onSurface = Black
)
private val AppTypography = Typography(
    bodyLarge = TextStyle(
        color = Black,
        textAlign = TextAlign.Start
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        color = Black
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        color = Black,
        textAlign = TextAlign.Center
    )
)

@Composable
fun TiendaHotWheelsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography
    ) {
        CompositionLocalProvider(
            LocalContentColor provides Black,
            LocalTextStyle provides LocalTextStyle.current.copy(color = Black)
        ) {
            content()
        }
    }
}
