package io.ducket.android.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.lang.IllegalStateException
import javax.inject.Inject

data class UserPreferences(
    val id: Long,
    val token: String,
)

fun UserPreferences.userPreferencesStored() = id > 0 && token.isNotEmpty()

class AppDataStore @Inject constructor(
    private val context: Context,
) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ducket_preferences")
        val userTokenKey = stringPreferencesKey(name = "user_token_key")
        val userIdKey = longPreferencesKey(name = "user_id_key")
    }

    private val dataStore = context.dataStore

    suspend fun persistUser(id: Long, token: String) {
        dataStore.edit {
            it[userIdKey] = id
            it[userTokenKey] = token
        }
    }

    suspend fun clearAll() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun clearUser() {
        dataStore.edit {
            it.remove(userTokenKey)
            it.remove(userIdKey)
        }
    }

    fun getUser(): UserPreferences {
        return runBlocking {
             dataStore.data.map {
                val token = it[userTokenKey] ?: ""
                val id = it[userIdKey] ?: -1
                UserPreferences(id, token)
            }.first()
        }
    }

    fun getUserPreferences(): Flow<UserPreferences> {
        return dataStore.data.map {
            val token = it[userTokenKey] ?: ""
            val id = it[userIdKey] ?: -1
            UserPreferences(id, token)
        }
    }
}