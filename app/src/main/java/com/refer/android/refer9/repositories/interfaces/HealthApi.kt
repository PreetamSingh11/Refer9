package com.refer.android.refer9.repositories.interfaces

import com.refer.android.refer9.models.DoctorRegistrationRequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HealthApi {
    @POST("doctor")
    fun registerDoctor(@Body doctorRegistrationRequestBody: DoctorRegistrationRequestBody): Call<Unit>
}