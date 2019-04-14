package com.refer.android.refer9.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.refer.android.refer9.models.DoctorRegistrationRequestBody
import com.refer.android.refer9.models.MessageEvent
import com.refer.android.refer9.repositories.interfaces.HealthApi
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class HealthRegisterRepository {
    val docRegisterResponse = MutableLiveData<Int>()
    private val baseURL = "http://166.62.127.160:8080/api/"
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

    fun doctorRegistration(requestBody: DoctorRegistrationRequestBody): LiveData<Int> {
        val client = getClient().create(HealthApi::class.java)
        val call = client.registerDoctor(requestBody)

        call.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                val errMsg = if (t is SocketTimeoutException) {
                    "Socket Time out. Please try again."

                } else {
                    t.localizedMessage
                }
                EventBus.getDefault().post(MessageEvent(errMsg))
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("ViewModel-singIn()", "token : ${response.body()}")
                docRegisterResponse.value = response.code()
            }
        })
        return docRegisterResponse
    }
}
