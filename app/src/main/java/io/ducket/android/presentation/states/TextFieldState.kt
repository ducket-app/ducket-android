package io.ducket.android.presentation.states

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ducket.android.R
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class TextFieldState(
    open val validator: (String) -> Boolean = { it.trim().isNotEmpty() },
    open val errorMessage: (String) -> String = { "Invalid input value" },
    open val maxLength: Int = 16,
) : Parcelable {

    // was the TextField ever focused
    @IgnoredOnParcel
    var focusedDirty by mutableStateOf(false)

    @IgnoredOnParcel
    var focused by mutableStateOf(false)

    @IgnoredOnParcel
    var enabled by mutableStateOf(true)

    @IgnoredOnParcel
    var value by mutableStateOf("")

    open val valid: Boolean get() = validator(value)
    open val focusReleased: Boolean get() = focusedDirty && !focused
    open val error: String get() = errorMessage(value)
    open val showError: Boolean get() = focusedDirty && !valid

    fun onFocusChange(focused: Boolean) {
        this.focused = focused
    }

    fun onValueChange(newValue: String) {
        if (newValue.length <= maxLength) {
            this.value = newValue
        }

        // set focusedDirty if the field focused first time and set some text
        if (focused && !focusedDirty && newValue.isNotEmpty()) {
            focusedDirty = true
        }
    }
}