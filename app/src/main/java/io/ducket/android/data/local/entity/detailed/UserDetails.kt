package io.ducket.android.data.local.entity.detailed

import androidx.room.Embedded
import androidx.room.Relation
import io.ducket.android.data.local.entity.CurrencyEntity
import io.ducket.android.data.local.entity.UserEntity

data class UserDetails(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id",
    )
    val currency: CurrencyEntity,
)