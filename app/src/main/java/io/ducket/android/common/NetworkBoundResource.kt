package io.ducket.android.common

import com.google.gson.Gson
import io.ducket.android.data.remote.dto.ErrorResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

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

            if (remoteResult is Response<*> && !remoteResult.isSuccessful){
                throw HttpException(remoteResult)
            }

            saveRemoteResult(remoteResult)
            onSuccess()

            loading.cancel()
            localQuery().collect { send(ResourceState.Success(it)) }
        } catch (e: Throwable) {
            val errState = when (e) {
                is HttpException -> {
                    println(e.message)
                    Gson().fromJson(e.response()?.errorBody()?.charStream(), ErrorResponse::class.java)?.message?.let { msg ->
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
                    println(e.message)
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
