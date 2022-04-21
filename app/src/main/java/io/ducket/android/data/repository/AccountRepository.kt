package io.ducket.android.data.repository

import androidx.room.withTransaction
import io.ducket.android.common.ResourceState
import io.ducket.android.common.networkBoundResourceChannel
import io.ducket.android.data.local.AppDataStore
import io.ducket.android.data.local.LocalDataSource
import io.ducket.android.data.local.entity.detailed.AccountDetails
import io.ducket.android.data.remote.RemoteDataSource
import io.ducket.android.domain.repository.IAccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val api: RemoteDataSource,
    private val db: LocalDataSource,
    private val datastore: AppDataStore,
) : IAccountRepository {

    override fun getAccount(id: Long): Flow<ResourceState<AccountDetails?>> {
        TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    override fun getAccounts(): Flow<ResourceState<List<AccountDetails>>> = flow {
        val userPrefs = datastore.getUserPreferences().first()

        val resourceStateFlow = networkBoundResourceChannel(
            remoteCall = {
                delay(1500)
                api.getAccounts(token = userPrefs.token)
            },
            isLocalDataStale = { accountEntities ->
                true
            },
            saveRemoteResult = { dto ->
                db.withTransaction {
                    db.insertRemoteAccount(*dto.toTypedArray())
                }
            },
            localQuery = {
                db.accountDao().selectAccounts()
            },
        )

        emitAll(resourceStateFlow)
    }
}