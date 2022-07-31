package io.ducket.android.data.remote

import io.ducket.android.data.remote.dto.*
import io.ducket.android.data.remote.dto.account.AccountResponse
import io.ducket.android.data.remote.dto.user.UserAuth
import io.ducket.android.data.remote.dto.user.UserCreate
import io.ducket.android.data.remote.dto.user.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface RemoteDataSource {

    companion object {
        const val BASE_URL = "http://192.168.0.199:8090/api/"
    }

    @POST("users/sign-up")
    suspend fun createUser(@Body payload: UserCreate): Response<UserResponse>

    @POST("users/sign-in")
    suspend fun authUser(@Body payload: UserAuth): Response<UserResponse>

    @GET("users/{userId}")
    suspend fun getUser(@Header("Authorization") token: String, @Path("userId") userId: Long): UserResponse

    @GET("accounts")
    suspend fun getAccounts(@Header("Authorization") token: String): List<AccountResponse>

    @GET("accounts/{accountId}")
    suspend fun getAccount(@Header("Authorization") token: String, @Path("accountId") accountId: Long): AccountResponse

    @GET("currencies")
    suspend fun getCurrencies(): List<CurrencyResponse>
}