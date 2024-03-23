package com.project.storyappproject.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.project.storyappproject.data.api.ApiService
import com.project.storyappproject.data.model.response.PostResponse
import okio.Timeout
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = RegisterViewModel()
    }

    @Test
    fun `user register success`() {
        val postResponse = PostResponse(error = false, message = "Success", description = "berhasil register")
        val call = MockCall(postResponse)
        Mockito.`when`(apiService.postRegister(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(call)

        val isLoadingObserver = Observer<Boolean> {}
        val isSuccessObserver = Observer<Boolean> {}
        val isErrorObserver = Observer<Boolean> {}
        val registerResultObserver = Observer<PostResponse> {}

        viewModel.isLoading.observeForever(isLoadingObserver)
        viewModel.isSuccess.observeForever(isSuccessObserver)
        viewModel.isError.observeForever(isErrorObserver)
        viewModel.registerResult.observeForever(registerResultObserver)

        viewModel.userRegister("FarhanTest", "passwordTest123456789", "farhanTest@example.com")

        assertEquals(true, viewModel.isLoading.value)
        assertEquals(false, viewModel.isSuccess.value)
        assertEquals(false, viewModel.isError.value)

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(true, viewModel.isSuccess.value)
        assertEquals(false, viewModel.isError.value)
        assertEquals(postResponse, viewModel.registerResult.value)

        viewModel.isLoading.removeObserver(isLoadingObserver)
        viewModel.isSuccess.removeObserver(isSuccessObserver)
        viewModel.isError.removeObserver(isErrorObserver)
        viewModel.registerResult.removeObserver(registerResultObserver)
    }

    private class MockCall<T>(private val response: T) : Call<T> {
        override fun enqueue(callback: Callback<T>) {
            callback.onResponse(this, Response.success(response))
        }

        override fun clone(): Call<T> = this
        override fun execute(): Response<T> = Response.success(response)
        override fun isExecuted(): Boolean = true
        override fun cancel() {}
        override fun isCanceled(): Boolean = false
        override fun request() = null
        override fun timeout(): Timeout = Timeout.NONE
    }
}