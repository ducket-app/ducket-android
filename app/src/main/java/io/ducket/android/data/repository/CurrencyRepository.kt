package io.ducket.android.data.repository

import androidx.room.withTransaction
import io.ducket.android.common.ResourceState
import io.ducket.android.common.networkBoundResourceChannel
import io.ducket.android.data.local.AppDataStore
import io.ducket.android.data.local.LocalDataSource
import io.ducket.android.data.local.entity.Currency
import io.ducket.android.data.remote.RemoteDataSource
import io.ducket.android.domain.repository.ICurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val api: RemoteDataSource,
    private val db: LocalDataSource,
) : ICurrencyRepository {

    @ExperimentalCoroutinesApi
    override fun getCurrencies(): Flow<ResourceState<List<Currency>>> = networkBoundResourceChannel(
        remoteCall = { api.getCurrencies() },
        localQuery = {
            db.currencyDao().selectCurrencies()
        },
        isLocalDataStale = { it.isEmpty() },
        saveRemoteResult = { dto ->
            db.withTransaction {
                db.insertRemoteCurrencies(dto)
            }
        },
    )

    @ExperimentalCoroutinesApi
    override fun searchCurrencies(query: String): Flow<ResourceState<List<Currency>>> = networkBoundResourceChannel(
        remoteCall = { api.getCurrencies() },
        localQuery = {
            db.currencyDao().searchCurrency(query = query)
        },
        isLocalDataStale = { false },
        saveRemoteResult = { dto ->
            db.withTransaction {
                db.insertRemoteCurrencies(dto)
            }
        }
    )
}