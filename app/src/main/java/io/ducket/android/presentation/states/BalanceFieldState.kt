package io.ducket.android.presentation.states

import kotlinx.parcelize.Parcelize

@Parcelize
data class BalanceFieldState(
    override val validator: (String) -> Boolean = { it.toFloatOrNull() != null },
    override val maxLength: Int = 16,
) : TextFieldState(validator)
