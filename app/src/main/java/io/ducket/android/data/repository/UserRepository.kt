package io.ducket.android.data.repository

import androidx.room.withTransaction
import io.ducket.android.common.ResourceState
import io.ducket.android.common.networkBoundResourceChannel
import io.ducket.android.data.local.AppDataStore
import io.ducket.android.data.local.LocalDataSource
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.data.remote.RemoteDataSource
import io.ducket.android.data.remote.dto.*
import io.ducket.android.domain.repository.IUserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: RemoteDataSource,
    private val db: LocalDataSource,
    private val datastore: AppDataStore,
) : IUserRepository {

    @ExperimentalCoroutinesApi
    override fun createUser(payload: UserCreateDto): Flow<ResourceState<UserDetails?>> = networkBoundResourceChannel(
        remoteCall = {
            delay(500)
            api.createUser(payload)
        },
        saveRemoteResult = { res ->
            val dto = res.body()!!
            db.withTransaction {
                db.insertRemoteUser(dto)
            }
            datastore.persistUserPreferences(dto.id, res.headers()["Authorization"]!!)
        },
        localQuery = {
            db.userDao().selectUser(payload.email)
        },
    )

    @ExperimentalCoroutinesApi
    override fun authUser(payload: UserAuthDto): Flow<ResourceState<UserDetails?>> = networkBoundResourceChannel(
        remoteCall = {
            api.authUser(payload)
        },
        saveRemoteResult = { res ->
            val dto = res.body()!!
            db.withTransaction {
                db.insertRemoteUser(dto)
            }
            datastore.persistUserPreferences(dto.id, res.headers()["Authorization"]!!)
        },
        localQuery = {
            db.userDao().selectUser(payload.email)
        },
    )

    @ExperimentalCoroutinesApi
    override fun getUser(): Flow<ResourceState<UserDetails?>> = flow {
        val userPrefs = datastore.getUserPreferences().first()

        val resourceStateFlow = networkBoundResourceChannel(
            remoteCall = {
                api.getUser(token = userPrefs.token, userId = userPrefs.id)
            },
            isLocalDataStale = { userEntity ->
                userEntity == null
            },
            saveRemoteResult = { dto ->
                db.withTransaction {
                    db.insertRemoteUser(dto)
                }
            },
            localQuery = {
                db.userDao().selectUser("test1@test.com")
            },
        )

        emitAll(resourceStateFlow)
    }

//    override fun getUser(): Flow<ResourceState<UserEntity>> = flow {
//        try {
//            val data = api.getUserDetails(token = token, userId = "2d5a3e15-3889-4a52-9da2-8b133cc37f83")
//            emit(ResourceState.Success<UserEntity>(data.toEntity(token)))
//        } catch (e: Throwable) {
//            emit(ResourceState.Error<UserEntity>(data = null, msg = "Error"))
//        }
//    }
}