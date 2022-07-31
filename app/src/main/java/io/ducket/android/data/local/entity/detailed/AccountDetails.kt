package io.ducket.android.data.local.entity.detailed

import androidx.room.Embedded
import androidx.room.Relation
import io.ducket.android.data.local.entity.AccountEntity
import io.ducket.android.data.local.entity.CurrencyEntity
import io.ducket.android.data.local.entity.UserEntity

data class AccountDetails(
    @Embedded
    val account: AccountEntity,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id",
    )
    val currency: CurrencyEntity,

    @Relation(
        parentColumn = "userId",
        entityColumn = "id",
        entity = UserEntity::class,
    )
    val user: UserDetails,
)
