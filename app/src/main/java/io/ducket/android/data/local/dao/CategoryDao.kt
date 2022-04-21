package io.ducket.android.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.ducket.android.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun selectCategories(): Flow<List<Category>>
}