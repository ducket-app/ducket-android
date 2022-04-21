package io.ducket.android.data.local.dao

import androidx.room.*
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.data.local.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Transaction
    @Query("SELECT * FROM user WHERE user.email = :email")
    fun selectUser(email: String): Flow<UserDetails?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}