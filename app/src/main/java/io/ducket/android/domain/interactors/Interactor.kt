package io.ducket.android.domain.interactors

import com.google.gson.Gson
import io.ducket.android.common.ResourceState
import io.ducket.android.data.remote.dto.ErrorDto
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

abstract class Interactor {

//    suspend fun <T> fetch(call: suspend() -> T): ResourceState<T> {
//        val res = call.invoke()
//
//        return try {
//            ResourceState.Success<T>(data = res)
//        } catch (e: Throwable) {
//            when (e) {
//                is HttpException -> {
//                    Gson().fromJson(e.response()?.errorBody()?.charStream(), ErrorDto::class.java)?.message?.let { msg ->
//                        if (e.response()?.code() in 400..499) {
//                            ResourceState.AuthorizationError<T>(msg = msg)
//                        } else {
//                            ResourceState.Error<T>(data = res, msg = msg)
//                        }
//                    } ?: ResourceState.Error<T>(data = res)
//                }
//                is IOException -> {
//                    ResourceState.ConnectivityError<T>()
//                }
//                else -> {
//                    ResourceState.Error<T>()
//                }
//            }
//        }
//    }
}