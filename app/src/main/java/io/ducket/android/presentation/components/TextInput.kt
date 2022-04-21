package io.ducket.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.ducket.android.R
import io.ducket.android.presentation.states.PasswordFieldState
import io.ducket.android.presentation.states.TextFieldState

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    readOnly: Boolean = false,
    error: String = stringResource(id = R.string.invalid_input_data_error),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colors.primary,
        cursorColor = MaterialTheme.colors.primary,
        focusedLabelColor = MaterialTheme.colors.primary,
    ),
    onPositioned: (LayoutCoordinates) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .height(68.dp)
                .fillMaxWidth()
                .onFocusChanged { state.onFocusChange(it.isFocused) }
                .onGloballyPositioned { onPositioned(it) },
            maxLines = maxLines,
            isError = state.showError,
            enabled = state.enabled,
            value = state.value,
            readOnly = readOnly,
            singleLine = singleLine,
            label = label,
            placeholder = placeholder,
            onValueChange = { state.onValueChange(it) },
            trailingIcon = trailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            colors = colors,
        )
        Box(
            modifier = Modifier
                .requiredHeight(16.dp)
                .padding(start = 16.dp, end = 12.dp)
        ) {
            if (state.showError) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error,
                )
            }
        }
    }
}

@Composable
fun SelectableTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    label: String,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit = {},
) {
    AppTextField(
        modifier = modifier
            .focusable(false)
            .clickable(false) {},
        state = state,
        maxLines = 1,
        readOnly = true,
        singleLine = true,
        label = {
            // while the field is not focusable by default, there is no way to show placeholder
            // other then replace label with placeholder value while field is empty
            Text(
                text = if (state.value.isNotEmpty()) label else placeholder
            )
        },
        trailingIcon = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = trailingIcon.name
                )
            }

        },
    )
}

@ExperimentalComposeUiApi
@Composable
fun ProtectedTextField(
    modifier: Modifier = Modifier,
    state: PasswordFieldState,
    error: String = stringResource(id = R.string.invalid_input_data_error),
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Next,
    onPositioned: (LayoutCoordinates) -> Unit = {},
) {
    val transformation = if (state.visible) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    AppTextField(
        modifier = modifier.fillMaxWidth(),
        state = state,
        label = label,
        error = error,
        placeholder = placeholder,
        visualTransformation = transformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction,
        ),
        onPositioned = {
            onPositioned(it)
        },
        keyboardActions = keyboardActions,
        trailingIcon = {
            val image = if (transformation == VisualTransformation.None) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }

            IconButton(
                onClick = { state.onVisibilityChange() }
            ) {
                Icon(
                    imageVector = image,
                    contentDescription = stringResource(id = R.string.visibility_desc)
                )
            }
        },
    )
}