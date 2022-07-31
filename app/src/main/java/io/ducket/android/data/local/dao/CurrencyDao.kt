package io.ducket.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ducket.android.data.local.entity.CurrencyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency WHERE currency.id = :id")
    fun selectCurrency(id: Long): Flow<CurrencyEntity>

    @Query("SELECT * FROM currency WHERE LOWER(isoCode) LIKE LOWER('%' || :query || '%')")
    fun searchCurrency(query: String): Flow<List<CurrencyEntity>>

    @Query("SELECT * FROM currency")
    fun selectCurrencies(): Flow<List<CurrencyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: CurrencyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)
}