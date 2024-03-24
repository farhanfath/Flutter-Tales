package com.project.storyappproject.ui.home.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){

    private val themeKey = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var Instance: SettingPreferences?= null
        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return Instance ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                Instance = instance
                instance
            }
        }
    }
}