package io.ducket.android.presentation.components.validator

import io.ducket.android.presentation.components.UiText

data class ValidationResult(
    val isValid: Boolean,
    val message: UiText? = null
)
