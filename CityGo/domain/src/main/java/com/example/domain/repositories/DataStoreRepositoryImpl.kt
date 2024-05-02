package com.example.domain.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.domain.interfaces.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repositories.DataStoreRepositoryImpl.PreferencesKey.userId
import com.example.domain.repositories.DataStoreRepositoryImpl.PreferencesKey.userRole
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject



class DataStoreRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>)
    :DataStoreRepository {


    private object PreferencesKey {
        val userId = stringPreferencesKey("user_id")
        val userRole = stringPreferencesKey("role")


    }

    override suspend fun setUserId(user_id: String) {
        Result.runCatching {
            dataStore.edit { preferences ->
                preferences[userId] = user_id
            }

        }}

        override suspend fun getUserId(): Result<String> {
            return Result.runCatching {
                val flow = dataStore.data
                    .catch { exception ->
                        /*
                     * dataStore.data throws an IOException when an error
                     * is encountered when reading data
                     */
                        if (exception is IOException) {
                            emit(emptyPreferences())
                        } else {
                            throw exception
                        }
                    }
                    .map { preferences ->
                        // Get our name value, defaulting to "" if not set
                        preferences[userId]
                    }
                val value = flow.firstOrNull() ?: "" // we only care about the 1st value
                value
            }
        }


    override suspend fun clearUserId() {
        Result.runCatching {
            dataStore.edit { preferences ->
                preferences.remove(PreferencesKey.userId)
                preferences.remove(PreferencesKey.userRole)
            }
        }

    }

    override suspend fun setUserRole(userR: String) {
        Result.runCatching {
            dataStore.edit { preferences ->
                preferences[userRole] = userR
            }

        }
    }

    override suspend fun getUserRole(): Result<String> {
        return Result.runCatching {
            val flow = dataStore.data
                .catch { exception ->
                    /*
                 * dataStore.data throws an IOException when an error
                 * is encountered when reading data
                 */
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    // Get our name value, defaulting to "" if not set
                    preferences[userRole]
                }
            val value = flow.firstOrNull() ?: "" // we only care about the 1st value
            value
        }
    }

}

