package io.ducket.android.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ducket.android.data.local.AppDataStore
import io.ducket.android.data.local.LocalDataSource
import io.ducket.android.data.remote.RemoteDataSource
import io.ducket.android.data.repository.AccountRepository
import io.ducket.android.data.repository.CurrencyRepository
import io.ducket.android.data.repository.UserRepository
import io.ducket.android.domain.repository.IAccountRepository
import io.ducket.android.domain.repository.ICurrencyRepository
import io.ducket.android.domain.repository.IUserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.OkHttpClient

import okhttp3.Interceptor
import okhttp3.Request


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        val interceptor = Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("User-Agent", "ducket-android-app")
                    .build()
            )
        }

        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.interceptors().add(interceptor)

        return Retrofit.Builder()
            .baseUrl(RemoteDataSource.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
            .create(RemoteDataSource::class.java)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        @ApplicationContext context: Context
    ): LocalDataSource = Room.databaseBuilder(
        context,
        LocalDataSource::class.java,
        "ducket_db",
    ).build()

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteSource: RemoteDataSource,
        localSource: LocalDataSource,
        appDataStore: AppDataStore,
    ): IUserRepository {
        return UserRepository(remoteSource, localSource, appDataStore)
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        remoteSource: RemoteDataSource,
        localSource: LocalDataSource,
        appDataStore: AppDataStore,
    ): IAccountRepository {
        return AccountRepository(remoteSource, localSource, appDataStore)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        remoteSource: RemoteDataSource,
        localSource: LocalDataSource,
    ): ICurrencyRepository {
        return CurrencyRepository(remoteSource, localSource)
    }

    @Provides
    @Singleton
    fun provideAppDataStore(
        @ApplicationContext context: Context
    ): AppDataStore = AppDataStore(context)
}