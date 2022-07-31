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

    override fun getAccount(id: Long): Flow<ResourceState<AccountDetails?>> = flow {
        val user = datastore.getUser()

        networkBoundResourceChannel(
            remoteCall = {
                api.getAccount(token = user.token, accountId = id)
            },
            isLocalDataStale = {
                true
            },
            saveRemoteResult = { dto ->
                db.withTransaction {
                    db.insertRemoteAccount(dto)
                }
            },
            localQuery = {
                db.accountDao().selectAccount(id)
            },
        ).also { emitAll(it) }
    }

    override fun getAccounts(): Flow<ResourceState<List<AccountDetails>>> = flow {
        val user = datastore.getUser()

        val resourceStateFlow = networkBoundResourceChannel(
            remoteCall = {
                api.getAccounts(token = user.token)
            },
            isLocalDataStale = {
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