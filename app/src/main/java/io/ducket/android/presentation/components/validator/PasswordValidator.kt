package io.ducket.android.presentation.components.validator

import io.ducket.android.R
import io.ducket.android.presentation.components.UiText

object PasswordValidator : Validator {

    override fun validate(value: String): ValidationResult {
        return if (value.length >= 4) {
            ValidationResult(true)
        } else {
            ValidationResult(false, UiText.StringResource(R.string.invalid_password_error))
        }
    }
}