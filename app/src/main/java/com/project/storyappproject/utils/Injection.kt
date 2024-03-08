package com.project.storyappproject.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.project.storyappproject.data.AppRepository
import com.project.storyappproject.data.UserPreferences
import com.project.storyappproject.data.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("token")

object Injection {
    fun provideRepository(context: Context) : AppRepository {
        val apiService = ApiConfig.getApiService()
        val preferences = UserPreferences.getInstance(context.dataStore)
        return AppRepository.getInstance(apiService, preferences)
    }
}