package com.refer.android.refer9.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.refer.android.refer9.models.DoctorRegistrationRequestBody
import com.refer.android.refer9.repositories.interfaces.HealthApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HealthRegisterRepository {
    val docRegisterResponse = MutableLiveData<Int>()
    private val baseURL = "http://166.62.127.160:8080/api/"
    private lateinit var retrofit: Retrofit

    private fun getClient(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    fun doctorRegistration(requestBody: DoctorRegistrationRequestBody): LiveData<Int> {
        val client = getClient().create(HealthApi::class.java)
        val call = client.registerDoctor(requestBody)

        call.enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("ViewModel", "Error : ${t.localizedMessage}")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("ViewModel-singIn()", "token : ${response.body()}")
                docRegisterResponse.value = response.code()
            }
        })
        return docRegisterResponse
    }
}
