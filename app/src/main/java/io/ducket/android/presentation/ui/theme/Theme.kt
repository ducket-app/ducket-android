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
    primary = Blue100,
    primaryVariant = Blue200,
    secondary = Orange100,
    secondaryVariant = Orange200,

    onPrimary = Black,
    onSecondary = Black,
    onBackground = White,
    onSurface = White,
)

private val LightColorPalette = lightColors(
    primary = Blue600,
    primaryVariant = Blue900,
    secondary = Orange400,
    secondaryVariant = Orange700,

    background = Gray100,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onBackground = Black,
    onSurface = Black,
)

val Colors.subtitle: Color
    @Composable
    get() = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)

@Composable
fun DucketAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isPreview: Boolean = true,
    content: @Composable () -> Unit
) {
    if (!isPreview) {
        val systemUiController = rememberSystemUiController()

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = if (darkTheme) Color.Black else Color.White
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