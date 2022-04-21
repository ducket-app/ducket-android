package io.ducket.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val email: String,
    val currencyId: Long,
//    val modifiedAt: String,
//    val createdAt: String,
)
