package io.ducket.android.data.remote.dto

import io.ducket.android.data.local.entity.Account
import java.math.BigDecimal

data class AccountDto(
    val id: Long,
    val owner: UserDto,
    val name: String,
    val balance: BigDecimal,
    val numOfRecords: Int,
    val accountType: String,
    val accountCurrency: CurrencyDto,
    val notes: String?,
)

fun AccountDto.toEntity(): Account {
    return Account(
        id = id,
        ownerId = owner.id,
        currencyId = accountCurrency.id,
        name = name,
        balance = balance,
        numOfRecords = numOfRecords,
        accountType = accountType,
        notes = notes,
    )
}