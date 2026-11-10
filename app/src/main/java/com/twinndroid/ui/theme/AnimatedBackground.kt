package com.twinndroid.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class Backdrop internal constructor()

val LocalBackdrop = staticCompositionLocalOf<Backdrop?> { null }

@Composable
fun rememberBackdrop(): Backdrop {
    return remember { Backdrop() }
}

/** Fixed black background. */
@Composable
fun AnimatedBackground(backdrop: Backdrop, modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .fillMaxSize()
            .background(Color.Black),
    )
}

@Composable
fun Frosted(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    tint: Color = Color.Transparent,
    blur: Dp = 0.dp,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(modifier) {
        if (tint.alpha > 0f) Box(Modifier.matchParentSize().background(tint))
        content()
    }
}
