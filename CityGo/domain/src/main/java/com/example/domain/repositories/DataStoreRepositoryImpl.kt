package com.example.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.example.domain.interfaces.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.domain.repositories.DataStoreRepositoryImpl.PreferencesKey.userId
import kotlinx.coroutines.flow.map



private const val USER_PREFERENCES_NAME = "user_preferences"
private const val USER_ID_KEY ="user_id"

class DataStoreRepositoryImpl constructor(private val dataStore: DataStore<androidx.datastore.preferences.core.Preferences>, context: Context)
    :DataStoreRepository {

    private val TAG: String = "UserPreferencesRepo"

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)

    private object PreferencesKey {
        val userId = stringPreferencesKey("user_id")


    }


    override suspend fun getUserId(userId: String) {
        sharedPreferences.edit {
            putString(USER_ID_KEY,userId)
        }
    }

    override suspend fun clearUserId() {
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(USER_ID_KEY,"")
        editor.apply()
    }

    override val savedUserId: Flow<String> = dataStore.data
            .map { preferences ->
                // Get our show completed value, defaulting to false if not set:
                preferences[userId] ?: ""

            }


}

