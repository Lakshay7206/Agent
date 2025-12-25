package com.example.agent.data.local.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenDataStore(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }


    val tokenFlow: Flow<String?> =
        dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }
}