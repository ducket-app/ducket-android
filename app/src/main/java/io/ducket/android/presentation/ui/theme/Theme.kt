package io.ducket.android.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Gray100,
    primaryVariant = Gray100,
    secondary = DeepPurple700,
    secondaryVariant = DeepPurple700,

    onPrimary = Black,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,
)

private val LightColorPalette = lightColors(
    primary = Gray900,
    primaryVariant = Gray900,
    secondary = DeepPurple700,
    secondaryVariant = DeepPurple700,

    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
)

val Colors.caption: Color @Composable get() = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
val Colors.success: Color @Composable get() = Green600
val Colors.failure: Color @Composable get() = Red

@Composable
fun DucketAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isPreview: Boolean = true,
    content: @Composable () -> Unit
) {
    if (!isPreview) {
        val systemUiController = rememberSystemUiController()

        SideEffect {
//            systemUiController.setSystemBarsColor(
//                color = if (darkTheme) Color.Black else Color.White,
//            )
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !darkTheme
            )
        }
    }

    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = DucketTypography,
        shapes = Shapes
    ) {
        CompositionLocalProvider(
            LocalRippleTheme provides AppRippleTheme,
            content = content
        )
    }
}

private object AppRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor(): Color = RippleTheme.defaultRippleColor(
        contentColor = Color(0xFF90CAF9),
        lightTheme = !isSystemInDarkTheme(),
    )

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        contentColor = Color.Black,
        lightTheme = !isSystemInDarkTheme(),
    )
}