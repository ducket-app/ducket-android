package io.ducket.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.ducket.android.data.local.dao.AccountDao
import io.ducket.android.data.local.dao.CategoryDao
import io.ducket.android.data.local.dao.CurrencyDao
import io.ducket.android.data.local.dao.UserDao
import io.ducket.android.data.local.entity.Account
import io.ducket.android.data.local.entity.Category
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.data.local.entity.User
import io.ducket.android.data.remote.dto.AccountDto
import io.ducket.android.data.remote.dto.CurrencyDto
import io.ducket.android.data.remote.dto.UserDto
import io.ducket.android.data.remote.dto.toEntity

@Database(
    entities = [User::class, Currency::class, Account::class, Category::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun currencyDao(): CurrencyDao

    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    suspend fun insertRemoteUser(userDto: UserDto) {
        insertRemoteCurrency(userDto.mainCurrency)
        userDao().insertUser(userDto.toEntity())
    }

    suspend fun insertRemoteCurrency(currencyDto: CurrencyDto) {
        currencyDao().insertCurrency(currencyDto.toEntity())
    }

    suspend fun insertRemoteCurrencies(currenciesDto: List<CurrencyDto>) {
        currencyDao().insertCurrencies(currenciesDto.map { it.toEntity() })
    }

    suspend fun insertRemoteAccount(vararg accountDto: AccountDto) {
        accountDto.distinctBy { it.owner.id }.forEach {
            insertRemoteUser(it.owner)
        }

        accountDto.distinctBy { it.accountCurrency.id }.forEach {
            insertRemoteCurrency(it.accountCurrency)
        }

        accountDao().insertAccounts(
            *accountDto.map { it.toEntity() }.toTypedArray()
        )
    }
}