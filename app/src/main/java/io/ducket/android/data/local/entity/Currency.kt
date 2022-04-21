package io.ducket.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey(autoGenerate = false)
    var id: Long,
    var name: String,
    var area: String,
    var symbol: String,
    var isoCode: String,
)