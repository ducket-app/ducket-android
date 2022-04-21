package io.ducket.android.data.remote.dto

data class UserCreateDto(
    val name: String,
    val email: String,
    val password: String,
    val currencyCode: String,
)