package com.example.flourish.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Estensione per il DataStore
val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferences(private val context: Context) {
    companion object {
        val USER_ID_KEY = longPreferencesKey("user_id")
    }

    // Recupera lo userId come Flow
    val userIdFlow: Flow<Long?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }

    // Salva lo userId
    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Rimuovi lo userId (ad esempio, durante il logout)
    suspend fun clearUserId() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
        }
    }
}
