package io.ducket.android.data.remote.dto.user

import androidx.compose.runtime.Immutable
import io.ducket.android.data.local.entity.UserEntity
import io.ducket.android.data.remote.dto.CurrencyResponse

@Immutable
data class UserResponse(
    val id: Long,
    val phone: String?,
    val name: String,
    val email: String,
    val currency: CurrencyResponse,
    val createdAt: Long,
    val modifiedAt: Long,
) {
    fun toEntity(): UserEntity {
        return UserEntity(
            id = id,
            name = name,
            phone = phone,
            email = email,
            currencyId = currency.id,
            createdAt = createdAt,
            modifiedAt = modifiedAt,
        )
    }
}
