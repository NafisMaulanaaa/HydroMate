package com.example.testhydromate.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.testhydromate.data.model.ReminderSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val ENABLED = booleanPreferencesKey("is_enabled")
        val VIBRATION = booleanPreferencesKey("is_vibration")
        val SOUND = booleanPreferencesKey("is_sound")
        val START_TIME = stringPreferencesKey("start_time")
        val END_TIME = stringPreferencesKey("end_time")
        val INTERVAL = intPreferencesKey("interval")
        val REPEAT_DAYS = stringSetPreferencesKey("repeat_days")
    }

    val reminderSettings: Flow<ReminderSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs ->
            ReminderSettings(
                isEnabled = prefs[Keys.ENABLED] ?: true,
                isVibration = prefs[Keys.VIBRATION] ?: true,
                isSound = prefs[Keys.SOUND] ?: true,
                startTime = prefs[Keys.START_TIME] ?: "08:00",
                endTime = prefs[Keys.END_TIME] ?: "22:00",
                interval = prefs[Keys.INTERVAL] ?: 60,
                repeatDays = prefs[Keys.REPEAT_DAYS] ?: setOf("1","2","3","4","5","6","7")
            )
        }

    suspend fun updateSwitch(key: String, value: Boolean) {
        dataStore.edit { prefs ->
            when(key) {
                "enabled" -> prefs[Keys.ENABLED] = value
                "vibration" -> prefs[Keys.VIBRATION] = value
                "sound" -> prefs[Keys.SOUND] = value
            }
        }
    }

    suspend fun updateTime(isStart: Boolean, time: String) {
        dataStore.edit { prefs ->
            if (isStart) prefs[Keys.START_TIME] = time else prefs[Keys.END_TIME] = time
        }
    }

    suspend fun updateInterval(minutes: Int) {
        dataStore.edit { prefs -> prefs[Keys.INTERVAL] = minutes }
    }

    suspend fun updateDays(days: Set<String>) {
        dataStore.edit { prefs -> prefs[Keys.REPEAT_DAYS] = days }
    }
}