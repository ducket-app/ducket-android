package io.ducket.android.common

sealed class ResourceState<T>(
    val data: T,
    val msg: String = "",
) {
    class Success<T>(data: T) : ResourceState<T>(data = data)
    class Loading<T>(data: T) : ResourceState<T>(data = data)
    class Error<T>(data: T, msg: String = "Oops, something went wrong!") : ResourceState<T>(data = data, msg = msg)
    class ConnectivityError<T>(data: T, msg: String = "Unable to connect to the server") : ResourceState<T>(data = data, msg = msg)
    class AuthorizationError<T>(data: T, msg: String = "Authorization error") : ResourceState<T>(data = data, msg = msg)
}