package io.ducket.android.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.ducket.android.R
import io.ducket.android.presentation.screens.auth.sign_in.noRippleClickable
import io.ducket.android.presentation.states.PasswordFieldState
import io.ducket.android.presentation.states.TextFieldState
import io.ducket.android.presentation.ui.theme.DucketAndroidTheme
import io.ducket.android.presentation.ui.theme.failure


@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    hint: String? = null,
    error: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    length: Int = 128,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit = {},
    onClearFocus: () -> Unit = {},
    onTrailingIconClick: () -> Unit = {},
    onLeadingIconClick: () -> Unit = {},
    onPositioned: (LayoutCoordinates) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var focusedDirty by rememberSaveable { mutableStateOf(false) }
    val iconColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)

    val leadingIconComposable: (@Composable () -> Unit)? = if (leadingIcon != null) {
        {
            IconButton(onClick = onLeadingIconClick) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }
    } else null

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .height(68.dp)
                .fillMaxWidth()
                .noRippleClickable {
                    if (!enabled) onClick()
                }
                .onFocusChanged {
                    if (it.isFocused && !focusedDirty) {
                        focusedDirty = true
                    }

                    if (!it.isFocused && focusedDirty) {
                        onValueChange(value)
                        onClearFocus()
                    }
                }
                .onGloballyPositioned {
                    onPositioned(it)
                },
            isError = error != null,
            enabled = enabled,
            value = value,
            maxLines = 1,
            singleLine = true,
            label = {
                // if the field is not enabled by default, there is no way to show placeholder
                // other then replace label with placeholder value while field is empty
                Text(
                    text = if (hint != null && value.isEmpty()) hint else label
                )
            },
            placeholder = {
                if (hint != null) {
                    Text(text = hint)
                }
            },
            onValueChange = {
                if (it.length <= length) {
                    onValueChange(it)
                }
            },
            leadingIcon = leadingIconComposable,
            trailingIcon = {
                if (trailingIcon != null) {
                    IconButton(onClick = onTrailingIconClick) {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = iconColor
                        )
                    }
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                cursorColor = MaterialTheme.colors.secondary,
                focusedBorderColor = MaterialTheme.colors.secondary,
                focusedLabelColor = MaterialTheme.colors.secondary,
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                disabledBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                disabledLeadingIconColor = MaterialTheme.colors.onSurface,
                disabledTrailingIconColor = MaterialTheme.colors.onSurface,
                disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
                disabledPlaceholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
            ),
        )
        Box(modifier = Modifier
            .requiredHeight(16.dp)
            .padding(start = 16.dp, end = 12.dp)
        ) {
            if (error != null) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.failure,
                )
            }
        }
    }
}

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
fun AppReadonlyTextField(
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
            .clickable(true) {
                onClick()
            },
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
fun AppProtectedTextField(
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
        }
    )
}