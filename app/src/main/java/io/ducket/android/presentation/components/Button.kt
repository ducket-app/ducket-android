package io.ducket.android.presentation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ducket.android.presentation.states.ButtonState
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme


@ExperimentalAnimationApi
@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    state: ButtonState,
    onClick: () -> Unit,
) {
    AppButton(
        modifier = modifier.height(56.dp),
        text = text,
        state = state,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            disabledBackgroundColor = MaterialTheme.colors.onSurface
                .copy(alpha = 0.1f)
                .compositeOver(MaterialTheme.colors.surface),
            disabledContentColor = MaterialTheme.colors.onPrimary
                .copy(alpha = ContentAlpha.disabled),
        ),
        onClick = { onClick() }
    )
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    state: ButtonState,
    onClick: () -> Unit,
) {
    AppButton(
        modifier = modifier.height(56.dp),
        text = text,
        state = state,
        colors = ButtonDefaults.appSecondaryButtonColors(),
        onClick = { onClick() }
    )
}

@Composable
fun ButtonDefaults.appSecondaryButtonColors(): ButtonColors = buttonColors(
    backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
    contentColor = MaterialTheme.colors.primary,
    disabledBackgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor = MaterialTheme.colors.onPrimary.copy(alpha = ContentAlpha.disabled),
)

@Composable
fun ButtonDefaults.appButtonElevation(): ButtonElevation = elevation(
    defaultElevation = 0.dp,
    pressedElevation = 0.dp,
    disabledElevation = 0.dp,
)

@Composable
private fun AppButton(
    modifier: Modifier = Modifier,
    text: String,
    state: ButtonState,
    colors: ButtonColors,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.clickable { !state.clickable },
        enabled = state.enabled,
        onClick = { onClick() },
        colors = colors,
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.appButtonElevation(),
    ) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
private fun PrimaryButtonPreview() {
    DucketAndroidTheme {
        PrimaryButton(
            state = ButtonState(),
            text = "Primary",
            onClick = {},
        )
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
private fun SecondaryButtonPreview() {
    DucketAndroidTheme {
        SecondaryButton(
            state = ButtonState(),
            text = "Secondary",
            onClick = {},
        )
    }
}