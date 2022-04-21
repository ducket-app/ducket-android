package io.ducket.android.common

import com.google.gson.Gson
import io.ducket.android.data.local.entity.detailed.UserDetails
import io.ducket.android.data.remote.dto.ErrorDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.IllegalStateException

@ExperimentalCoroutinesApi
inline fun <LocalType, RemoteType> networkBoundResourceChannel(
    crossinline remoteCall: suspend () -> RemoteType,
    crossinline isLocalDataStale: (LocalType) -> Boolean = { true },
    crossinline saveRemoteResult: suspend (RemoteType) -> Unit,
    crossinline localQuery: () -> Flow<LocalType>,
    crossinline onSuccess: () -> Unit = { },
    crossinline onFailed: (String) -> Unit = { },
) = channelFlow {
    val localData = localQuery().first()

    if (isLocalDataStale(localData) || localData == null) {
        val loading = launch {
            localQuery().collect { send(ResourceState.Loading(it)) }
        }

        try {
            val remoteResult = remoteCall()

            if (remoteResult is Response<*> && !remoteResult.isSuccessful)
                throw HttpException(remoteResult)

            saveRemoteResult(remoteResult)
            onSuccess()

            loading.cancel()
            localQuery().collect { send(ResourceState.Success(it)) }
        } catch (e: Throwable) {
            val errState = when (e) {
                is HttpException -> {
                    Gson().fromJson(e.response()?.errorBody()?.charStream(), ErrorDto::class.java)?.message?.let { msg ->
                        if (e.response()?.code() in 400..499) {
                            ResourceState.AuthorizationError<LocalType>(data = localData, msg = msg)
                        } else {
                            ResourceState.Error<LocalType>(data = localData, msg = msg)
                        }
                    } ?: ResourceState.Error<LocalType>(data = localData)
                }
                is IOException -> {
                    ResourceState.ConnectivityError<LocalType>(data = localData)
                }
                else -> {
                    ResourceState.Error<LocalType>(data = localData)
                }
            }

            onFailed(errState.msg)
            loading.cancel()
            localQuery().collect { send(errState) }
        }
    } else {
        localQuery().collect { send(ResourceState.Success(it)) }
    }
}

//inline fun <LocalType, RemoteType> networkBoundResource(
//    crossinline remoteCall: suspend () -> RemoteType,
//    crossinline isLocalDataStale: (LocalType) -> Boolean = { true },
//    crossinline saveRemoteData: suspend (RemoteType) -> Unit,
//    crossinline localQuery: () -> Flow<LocalType>,
//) = flow {
//    val localData = localQuery().first()
//
//    val resourceStateFlow = if (isLocalDataStale(localData) || localData == null) {
//        emit(ResourceState.Loading())
//
//        try {
//            val remoteRes = remoteCall()
//            if (remoteRes is Response<*> && !remoteRes.isSuccessful)
//                throw HttpException(remoteRes)
//
//            saveRemoteData(remoteRes)
//            localQuery().map { ResourceState.Success(it) }
//        } catch (e: Throwable) {
//            Timber.e(e)
//
//            when (e) {
//                is HttpException -> {
//                    Gson().fromJson(e.response()?.errorBody()?.charStream(), ErrorDto::class.java)?.message?.let { msg ->
//                        if (e.response()?.code() in 400..499) {
//                            localQuery().map { ResourceState.AuthorizationError<LocalType>(msg = msg) }
//                        } else {
//                            localQuery().map { ResourceState.Error<LocalType>(data = localData, msg = msg) }
//                        }
//                    } ?: localQuery().map { ResourceState.Error<LocalType>(data = localData) }
//                }
//                is IOException -> {
//                    localQuery().map { ResourceState.ConnectivityError<LocalType>() }
//                }
//                else -> {
//                    localQuery().map { ResourceState.Error<LocalType>() }
//                }
//            }
//        }
//    } else {
//        localQuery().map { ResourceState.Success(it) }
//    }
//
//    emitAll(resourceStateFlow)
//}
