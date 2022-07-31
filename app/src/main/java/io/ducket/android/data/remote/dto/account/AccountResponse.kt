package io.ducket.android.data.remote.dto.account

import io.ducket.android.data.local.entity.AccountEntity
import io.ducket.android.data.remote.dto.CurrencyResponse
import java.math.BigDecimal

data class AccountResponse(
    val id: Long,
    val extId: String?,
    val userId: Long,
    val name: String,
    val currency: CurrencyResponse,
    val startBalance: BigDecimal,
    val totalBalance: BigDecimal,
    val type: AccountType,
    val notes: String?,
) {
    fun toEntity(): AccountEntity {
        return AccountEntity(
            id = id,
            userId = userId,
            currencyId = currency.id,
            name = name,
            startBalance = startBalance,
            totalBalance = totalBalance,
            type = type,
            notes = notes,
        )
    }
}
