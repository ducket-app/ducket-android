package io.ducket.android.data.local.entity.detailed

import androidx.room.Embedded
import androidx.room.Relation
import io.ducket.android.data.local.entity.Account
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.data.local.entity.User

data class AccountDetails(
    @Embedded
    val account: Account,

    @Relation(
        parentColumn = "currencyId",
        entityColumn = "id",
    )
    val currency: Currency,

    @Relation(
        parentColumn = "ownerId",
        entityColumn = "id",
        entity = User::class,
    )
    val owner: UserDetails,
)
