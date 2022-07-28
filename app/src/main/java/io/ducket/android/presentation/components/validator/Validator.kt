package io.ducket.android.presentation.components.validator

internal interface Validator {
    fun validate(value: String): ValidationResult
}