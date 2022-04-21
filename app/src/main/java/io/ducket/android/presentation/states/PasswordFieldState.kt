package io.ducket.android.presentation.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class PasswordFieldState(
    override val validator: (String) -> Boolean = { it.length in 4..16 },
    override val maxLength: Int = 16,
) : TextFieldState(validator) {

    @IgnoredOnParcel
    var visible by mutableStateOf(false)

    fun onVisibilityChange() {
        visible = !visible
    }
}