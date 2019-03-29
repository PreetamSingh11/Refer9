package com.refer.android.refer9.repositories.interfaces

import com.refer.android.refer9.models.UserProfile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ProfileApi {

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("me")
    fun getUserProfile(@Header("Authorization") token: String): Call<UserProfile>
}