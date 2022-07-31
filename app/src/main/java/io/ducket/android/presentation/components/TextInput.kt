package io.ducket.android.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.ducket.android.common.extensions.noRippleClickable
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
