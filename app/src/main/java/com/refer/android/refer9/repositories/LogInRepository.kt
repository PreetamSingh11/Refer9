package com.refer.android.refer9.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.refer.android.refer9.models.*
import com.refer.android.refer9.repositories.interfaces.LoginApi
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class LogInRepository {
    val logInResponse = MutableLiveData<LogInResponseBody>()
    val signUpResponse = MutableLiveData<SignUpResponseBody>()
    private val baseURL = "http://166.62.127.160:8080/auth/"
    private lateinit var retrofit: Retrofit

    private var client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private fun getClient(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun getSignIn(email: String, password: String): LiveData<LogInResponseBody> {
        val client = getClient().create(LoginApi::class.java)
        val call = client.loginUser(LogInRequestBody(email, password))

        call.enqueue(object : Callback<LogInResponseBody> {
            override fun onFailure(call: Call<LogInResponseBody>, t: Throwable) {
                val errMsg = if (t is SocketTimeoutException) {
                    "Socket Time out. Please try again."

                } else {
                    t.localizedMessage
                }
                EventBus.getDefault().post(MessageEvent(errMsg))
            }

            override fun onResponse(call: Call<LogInResponseBody>, responseBody: Response<LogInResponseBody>) {
                logInResponse.value = responseBody.body()
                Log.d("ViewModel-singIn()", "token : ${responseBody.body()?.accessToken}")
            }
        })

        return logInResponse
    }

    fun setSignUp(name: String, email: String, password: String): LiveData<SignUpResponseBody> {
        val client = getClient().create(LoginApi::class.java)
        val call = client.signUp(SignUpRequestBody(name, email, password))

        call.enqueue(object : Callback<SignUpResponseBody> {
            override fun onFailure(call: Call<SignUpResponseBody>, t: Throwable) {
                val errMsg = if (t is SocketTimeoutException) {
                    "Socket Time out. Please try again."

                } else {
                    t.localizedMessage
                }
                EventBus.getDefault().post(MessageEvent(errMsg))
            }

            override fun onResponse(call: Call<SignUpResponseBody>, response: Response<SignUpResponseBody>) {
                signUpResponse.value = response.body()
            }
        })
        return signUpResponse
    }

}