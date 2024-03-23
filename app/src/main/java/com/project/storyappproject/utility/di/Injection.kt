package com.project.storyappproject.utility.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.project.storyappproject.data.api.ApiConfig
import com.project.storyappproject.data.datapaging.StoryRepository
import com.project.storyappproject.data.datastore.UserPreference

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storiesin")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference(context)
        val apiService = ApiConfig().getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}