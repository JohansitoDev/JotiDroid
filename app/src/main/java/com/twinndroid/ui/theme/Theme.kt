package com.twinndroid.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

@Immutable
data class TwinnExtras(
    val headerBrush: Brush,
    val headerStart: Color,
    val headerEnd: Color,
    val background: BgAnimation = BgAnimation.NONE,
    val parallax: Boolean = false,
    val outlines: Boolean = true,
    val glassCards: Boolean = true,
    val header: HeaderSettings = HeaderSettings(),
)

val LocalTwinnExtras = staticCompositionLocalOf {
    TwinnExtras(Brush.linearGradient(listOf(Color.Black, Color(0xFF121212))), Color.Black, Color(0xFF121212))
}

private val TwinnType = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp, letterSpacing = (-0.5).sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
)

@Composable
fun TwinnTheme(config: ThemeConfig = ThemeConfig(), content: @Composable () -> Unit) {
    val scheme = darkColorScheme(
        background = Color.Black,
        surface = Color.Black,
        primary = Color(0xFF2962FF),
        onPrimary = Color.White,
        secondary = Color(0xFF00E676),
        onSecondary = Color.Black,
        tertiary = Color(0xFF00C853),
        onTertiary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White,
        surfaceVariant = Color(0xFF1E1E1E),
        onSurfaceVariant = Color(0xFFB0B0B0),
        surfaceContainerLowest = Color.Black,
        surfaceContainerLow = Color(0xFF101010),
        surfaceContainer = Color(0xFF161616),
        surfaceContainerHigh = Color(0xFF222222),
        surfaceContainerHighest = Color(0xFF2D2D2D),
        outline = Color(0xFF444444),
        outlineVariant = Color(0xFF2E2E2E),
    )

    val extras = TwinnExtras(
        headerBrush = Brush.linearGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E))),
        headerStart = Color(0xFF121212),
        headerEnd = Color(0xFF1E1E1E),
        background = BgAnimation.NONE,
        parallax = false,
        outlines = true,
        glassCards = true,
        header = config.header,
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalTwinnExtras provides extras,
        LocalDensity provides Density(density.density, density.fontScale * config.fontScale),
    ) {
        MaterialTheme(colorScheme = scheme, typography = TwinnType, content = content)
    }
}
