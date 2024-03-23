package com.project.storyappproject.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.storyappproject.data.api.ApiService
import com.project.storyappproject.data.model.response.AuthResponse
import com.project.storyappproject.data.model.response.LoginResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = LoginViewModel()
    }

    @Test
    fun `login success should update live data`() {
        val loginExample = generateExampleLogin()

        val mockCall = mock(Call::class.java) as Call<AuthResponse>
        `when`(apiService.postLogin("test@example.com", "password")).thenReturn(mockCall)

        viewModel.userLogin("test@example.com", "password")
        (mockCall as Callback<AuthResponse>).onResponse(mockCall, Response.success(loginExample))

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(false, viewModel.isError.value)
        assertEquals(true, viewModel.isSuccess.value)
        assertEquals(loginExample.loginResult, viewModel.loginResult.value)
    }

    @Test
    fun `login failure should update live data`() {
        val mockCall = mock(Call::class.java) as Call<AuthResponse>
        `when`(apiService.postLogin("test@example.com", "password")).thenReturn(mockCall)

        viewModel.userLogin("test@example.com", "password")
        (mockCall as Callback<AuthResponse>).onFailure(mockCall, Throwable())

        assertEquals(false, viewModel.isLoading.value)
        assertEquals(true, viewModel.isError.value)
        assertEquals(false, viewModel.isSuccess.value)
    }

    private fun generateExampleLogin(): AuthResponse {
        return AuthResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                userId = "userId",
                name = "name",
                token = "token"
            )
        )
    }
}