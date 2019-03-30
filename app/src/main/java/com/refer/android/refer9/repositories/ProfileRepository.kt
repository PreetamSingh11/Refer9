package com.refer.android.refer9.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.refer.android.refer9.models.UserProfile
import com.refer.android.refer9.repositories.interfaces.ProfileApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class ProfileRepository {

    private lateinit var retrofit: Retrofit
    val profileResponse = MutableLiveData<UserProfile>()

    private val baseURL = "http://166.62.127.160:8080/user/"
    private lateinit var token: String

    private var client = OkHttpClient.Builder().addInterceptor(object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
    }).build()

    private fun getClient(): Retrofit {
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun getProfile(userToken:String):LiveData<UserProfile>{
        token = userToken
        val client = getClient().create(ProfileApi::class.java)
        val call = client.getUserProfile("Bearer $token")

        call.enqueue(object : Callback<UserProfile> {
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                Log.d("ViewModel","Error : ${t.localizedMessage}")
                profileResponse.value=null
            }

            override fun onResponse(call: Call<UserProfile>, responseBody: retrofit2.Response<UserProfile>) {
                Log.d("Shared values in splash","isSuccessful : ${responseBody.isSuccessful}")
                Log.d("Shared values in splash","code : ${responseBody.code()}")
                profileResponse.value = responseBody.body()
            }
        })
        return profileResponse
    }
}