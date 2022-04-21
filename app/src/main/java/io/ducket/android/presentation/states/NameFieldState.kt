package io.ducket.android.presentation.states

import kotlinx.parcelize.Parcelize

@Parcelize
data class NameFieldState(
    override val validator: (String) -> Boolean = { it.trim().length in 2..32 },
    override val maxLength: Int = 32,
) : TextFieldState(validator)
