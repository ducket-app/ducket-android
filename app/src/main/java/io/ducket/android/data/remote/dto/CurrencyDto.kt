package io.ducket.android.data.remote.dto

import io.ducket.android.data.local.entity.Currency

data class CurrencyDto(
    val id: Long,
    val name: String,
    val symbol: String,
    val isoCode: String,
    val area: String,
)

fun CurrencyDto.toEntity(): Currency {
    return Currency(
        id = id,
        name = name,
        symbol = symbol,
        isoCode = isoCode,
        area = area,
    )
}