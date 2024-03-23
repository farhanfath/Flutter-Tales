package com.project.storyappproject.data.datapaging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.project.storyappproject.data.api.ApiService
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.response.ListStoryItem

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val pref: UserPreference,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        val token = pref.getUser().token.toString()

        try {
            val responseData = token.let { apiService.getPagingStories(
                "Bearer $it",
                page,
                state.config.pageSize,
                0
            ) }

            return if (responseData.isSuccessful) {
                val endOfPaginationReached = responseData.body()!!.listStory.isEmpty()
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                MediatorResult.Error(Exception("Failed load story"))
            }
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}