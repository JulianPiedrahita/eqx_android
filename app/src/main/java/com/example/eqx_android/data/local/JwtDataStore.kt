package com.example.eqx_android.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.jwtDataStore by preferencesDataStore(name = "jwt_prefs")

class JwtDataStore(private val context: Context) {
    companion object {
        val JWT_KEY = preferencesKey<String>("jwt_token")
    }


    suspend fun saveToken(token: String) {
        context.jwtDataStore.edit { prefs ->
            prefs[JWT_KEY] = token
        }
    }


    fun getToken(): Flow<String?> =
        context.jwtDataStore.data.map { prefs ->
            prefs[JWT_KEY]
        }

    suspend fun clearToken() {
        context.jwtDataStore.edit { prefs ->
            prefs.remove(JWT_KEY)
        }
    }
}
