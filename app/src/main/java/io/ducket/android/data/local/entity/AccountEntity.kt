package io.ducket.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ducket.android.data.remote.dto.account.AccountType
import java.math.BigDecimal

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val userId: Long,
    val currencyId: Long,
    val name: String,
    val type: AccountType,
    var startBalance: BigDecimal,
    var totalBalance: BigDecimal,
    val notes: String?,
)
