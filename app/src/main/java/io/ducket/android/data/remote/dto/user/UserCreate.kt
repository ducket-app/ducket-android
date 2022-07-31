package io.ducket.android.data.remote.dto.user

import io.ducket.android.data.remote.dto.account.AccountCreate

data class UserCreate(
    val name: String,
    val email: String,
    val password: String,
    val currency: String,
    val defaultAccount: AccountCreate?,
)