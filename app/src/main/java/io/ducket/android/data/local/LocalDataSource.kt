package io.ducket.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.ducket.android.data.local.dao.AccountDao
import io.ducket.android.data.local.dao.CategoryDao
import io.ducket.android.data.local.dao.CurrencyDao
import io.ducket.android.data.local.dao.UserDao
import io.ducket.android.data.local.entity.AccountEntity
import io.ducket.android.data.local.entity.CategoryEntity
import io.ducket.android.data.local.entity.CurrencyEntity
import io.ducket.android.data.local.entity.UserEntity
import io.ducket.android.data.remote.dto.CurrencyResponse
import io.ducket.android.data.remote.dto.user.UserResponse
import io.ducket.android.data.remote.dto.account.AccountResponse

@Database(
    entities = [UserEntity::class, CurrencyEntity::class, AccountEntity::class, CategoryEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun currencyDao(): CurrencyDao

    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    suspend fun insertRemoteUser(userDto: UserResponse) {
        currencyDao().insertCurrency(userDto.currency.toEntity())
        userDao().insertUser(userDto.toEntity())
    }

    suspend fun insertRemoteCurrency(currencyDto: CurrencyResponse) {
        currencyDao().insertCurrency(currencyDto.toEntity())
    }

    suspend fun insertRemoteCurrencies(currenciesDto: List<CurrencyResponse>) {
        currencyDao().insertCurrencies(currenciesDto.map { it.toEntity() })
    }

    suspend fun insertRemoteAccount(vararg accountResponse: AccountResponse) {
        accountResponse.distinctBy { it.currency.id }.forEach {
            insertRemoteCurrency(it.currency)
        }

        accountDao().insertAccounts(*accountResponse.map { it.toEntity() }.toTypedArray())
    }
}