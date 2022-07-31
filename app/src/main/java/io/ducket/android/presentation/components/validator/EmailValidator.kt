package io.ducket.android.presentation.components.validator

import android.util.Patterns
import io.ducket.android.R
import io.ducket.android.presentation.components.UiText

object EmailValidator : Validator {

    override fun validate(value: String): ValidationResult {
        return if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            ValidationResult(true)
        } else {
            ValidationResult(false, UiText.StringResource(R.string.invalid_email_error))
        }
    }
}