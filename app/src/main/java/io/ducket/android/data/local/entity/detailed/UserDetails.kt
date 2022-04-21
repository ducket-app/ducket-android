package io.ducket.android.data.local.entity.detailed

import androidx.room.Embedded
import androidx.room.Relation
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.data.local.entity.User

data class UserDetails(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id",
    )
    val currency: Currency,
)