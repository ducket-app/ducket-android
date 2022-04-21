package io.ducket.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val currencyId: Long,
    val ownerId: Long,
    val name: String,
    val accountType: String,
    var balance: BigDecimal,
    val numOfRecords: Int,
    val notes: String?,
)
