package com.project.storyappproject.data.api

import com.project.storyappproject.data.model.response.AuthResponse
import com.project.storyappproject.data.model.response.PostResponse
import com.project.storyappproject.data.model.response.PostStoriesResponse
import com.project.storyappproject.data.model.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("email") email: String
    ): Call<PostResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<AuthResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun postStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double,
        @Part("lon") lon: Double,
    ): Call<PostStoriesResponse>

}