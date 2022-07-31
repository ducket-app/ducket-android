package io.ducket.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ducket.android.domain.model.CategoryItem

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    var id: Long,
    var name: String,
    var group: String,
)