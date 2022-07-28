package io.ducket.android.presentation.components.validator

import io.ducket.android.R
import io.ducket.android.presentation.components.UiText

object CurrencyTextValidator : Validator {

    override fun validate(value: String): ValidationResult {
        return if (value.length == 3) {
            ValidationResult(true)
        } else {
            ValidationResult(false, UiText.StringResource(R.string.invalid_currency_error))
        }
    }
}