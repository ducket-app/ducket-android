package io.ducket.android.presentation.states

import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrencyFieldState(
    override val validator: (String) -> Boolean = { it.length == 3 },
) : TextFieldState(validator)
