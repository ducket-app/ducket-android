package io.ducket.android.data.local.dao

import androidx.room.*
import io.ducket.android.data.local.entity.Account
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.data.local.entity.detailed.AccountDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Transaction
    @Query("SELECT * FROM account WHERE account.id = :id")
    fun selectAccount(id: Long): Flow<AccountDetails>

    @Transaction
    @Query("SELECT * FROM account")
    fun selectAccounts(): Flow<List<AccountDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(vararg accounts: Account)
}