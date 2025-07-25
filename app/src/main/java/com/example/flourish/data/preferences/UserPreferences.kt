package com.example.flourish.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

// Estensione per il DataStore
val Context.dataStore by preferencesDataStore("user_preferences")

class UserPreferences(private val context: Context) {
    companion object {
        val USER_ID_KEY = longPreferencesKey("user_id")
        val PLANT_STAGE_KEY = intPreferencesKey("plant_stage")
        val PLANT_HEALTH_KEY = stringPreferencesKey("plant_health")
        val WEEK_UPDATED_KEY = stringPreferencesKey("week_updated")
        val FIRST_USE_DATE_KEY = stringPreferencesKey("first_use_date")
    }

    // Recupera lo userId come Flow
    val userIdFlow: Flow<Long?> = context.dataStore.data
        .map { preferences -> preferences[USER_ID_KEY] }

    val plantStageFlow: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[PLANT_STAGE_KEY] ?: 0 }

    val plantHealthFlow: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[PLANT_HEALTH_KEY] ?: "healthy" }

    val weekUpdatedFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[WEEK_UPDATED_KEY] }

    val firstUseDateFlow: Flow<LocalDate?> = context.dataStore.data
        .map { prefs ->
            prefs[FIRST_USE_DATE_KEY]?.let { LocalDate.parse(it) }
        }

    // Salva lo userId
    suspend fun saveUserId(userId: Long) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    // Rimuovi lo userId (ad esempio, durante il logout)
    suspend fun clearUserSata() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(PLANT_STAGE_KEY)
            preferences.remove(PLANT_HEALTH_KEY)
            preferences.remove(WEEK_UPDATED_KEY)
            preferences.remove(FIRST_USE_DATE_KEY)
        }
    }

    suspend fun savePlantStage(stage: Int) {
        context.dataStore.edit { preferences ->
            preferences[PLANT_STAGE_KEY] = stage
        }
    }

    suspend fun savePlantHealth(health: String) {
        context.dataStore.edit { preferences ->
            preferences[PLANT_HEALTH_KEY] = health
        }
    }

    suspend fun saveWeekUpdated(week: String) {
        context.dataStore.edit { preferences ->
            preferences[WEEK_UPDATED_KEY] = week
        }
    }

    suspend fun saveFirstUseDate(date: LocalDate) {
        context.dataStore.edit { preferences ->
            preferences[FIRST_USE_DATE_KEY] = date.toString()
        }
    }
}
