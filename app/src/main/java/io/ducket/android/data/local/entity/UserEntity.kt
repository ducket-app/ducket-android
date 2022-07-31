package io.ducket.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String,
    val currencyId: Long,
    val modifiedAt: Long,
    val createdAt: Long,
)
