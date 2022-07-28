package io.ducket.android.presentation.screens

import io.ducket.android.presentation.components.UiText

data class InputFieldState(
    val text: String = "",
    val error: UiText? = null,
    val enabled: Boolean = true,
)

data class ProtectedInputFieldState(
    val text: String = "",
    val error: UiText? = null,
    val visible: Boolean = false,
    val enabled: Boolean = true,
)