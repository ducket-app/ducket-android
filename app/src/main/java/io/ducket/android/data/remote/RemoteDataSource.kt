package io.ducket.android.data.remote

import io.ducket.android.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface RemoteDataSource {

    companion object {
        const val BASE_URL = "http://192.168.0.199:8090/api/"
    }

    @POST("users")
    suspend fun createUser(@Body payload: UserCreateDto): Response<UserDto>

    @POST("users/auth")
    suspend fun authUser(@Body payload: UserAuthDto): Response<UserDto>

    @GET("users/{userId}")
    suspend fun getUser(@Header("Authorization") token: String, @Path("userId") userId: Long): UserDto

    @GET("accounts")
    suspend fun getAccounts(@Header("Authorization") token: String): List<AccountDto>

    @GET("accounts/{accountId}")
    suspend fun getAccount(@Header("Authorization") token: String, @Path("accountId") accountId: Long): AccountDto

    @GET("currencies")
    suspend fun getCurrencies(): List<CurrencyDto>
}