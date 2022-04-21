package io.ducket.android.data.remote.dto

import androidx.compose.runtime.Immutable
import io.ducket.android.data.local.entity.User

@Immutable
data class UserDto(
    val id: Long,
    val name: String,
    val email: String,
    val mainCurrency: CurrencyDto,
    val modifiedAt: String,
    val createdAt: String,
)

fun UserDto.toEntity(): User {
    return User(
        id = id,
        name = name,
        email = email,
        currencyId = mainCurrency.id,
    )
}