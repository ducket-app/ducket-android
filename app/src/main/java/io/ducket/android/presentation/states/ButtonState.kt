package io.ducket.android.presentation.states

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class ButtonState : Parcelable {

    @IgnoredOnParcel
    var enabled by mutableStateOf(true)

    @IgnoredOnParcel
    val clickable get() = enabled
}
