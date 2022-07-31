package io.ducket.android.data.remote.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val name: String,
    val email: String,
    val password: String,
    val currency: String,
) : Parcelable
