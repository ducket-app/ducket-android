package io.ducket.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ducket.android.data.local.entity.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency WHERE currency.id = :id")
    fun selectCurrency(id: Long): Flow<Currency>

    @Query("SELECT * FROM currency WHERE LOWER(isoCode) LIKE LOWER('%' || :query || '%')")
    fun searchCurrency(query: String): Flow<List<Currency>>

    @Query("SELECT * FROM currency")
    fun selectCurrencies(): Flow<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currency: Currency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencies: List<Currency>)
}