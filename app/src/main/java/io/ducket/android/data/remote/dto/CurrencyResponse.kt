package io.ducket.android.data.remote.dto

import io.ducket.android.data.local.entity.CurrencyEntity

data class CurrencyResponse(
    val id: Long,
    val name: String,
    val symbol: String,
    val isoCode: String,
    val area: String,
) {
    fun toEntity(): CurrencyEntity {
        return CurrencyEntity(
            id = id,
            name = name,
            symbol = symbol,
            isoCode = isoCode,
            area = area,
        )
    }
}
