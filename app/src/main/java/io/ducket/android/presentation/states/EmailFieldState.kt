package io.ducket.android.presentation.states

import android.util.Patterns
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailFieldState(
    override val validator: (String) -> Boolean = { Patterns.EMAIL_ADDRESS.matcher(it).matches() },
    override val maxLength: Int = 32,
) : TextFieldState(validator)
