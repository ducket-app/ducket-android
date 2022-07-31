package io.ducket.android.presentation.components.validator

import io.ducket.android.R
import io.ducket.android.presentation.components.UiText

object NumberValidator : Validator {

    override fun validate(value: String): ValidationResult {
        return if (value.toBigDecimalOrNull() != null) {
            ValidationResult(true)
        } else {
            ValidationResult(false, UiText.StringResource(R.string.invalid_number_error))
        }
    }
}