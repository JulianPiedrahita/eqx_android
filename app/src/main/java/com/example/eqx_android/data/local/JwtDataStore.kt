package com.example.eqx_android.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class JwtDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "jwt_prefs")
        val JWT_KEY = preferencesKey<String>("jwt_token")
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[JWT_KEY] = token
        }
    }

    fun getToken(): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[JWT_KEY]
        }

    suspend fun clearToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(JWT_KEY)
        }
    }
}
