package com.project.storyappproject.data.datapaging

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.project.storyappproject.data.api.ApiService
import com.project.storyappproject.data.datapaging.database.StoryDatabase
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.response.ListStoryItem

class StoryRepository(private val context: Context, private val apiService: ApiService, private val storyDatabase: StoryDatabase) {
    fun getListStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(context, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }

        ).liveData
    }
}