package io.ducket.android.data.remote.dto.account

import java.math.BigDecimal

data class AccountCreate(
    val name: String,
    val type: AccountType,
    val currency: String,
    val startBalance: BigDecimal,
    val notes: String?,
)
