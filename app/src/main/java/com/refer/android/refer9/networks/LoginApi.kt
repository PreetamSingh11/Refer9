package com.refer.android.refer9.networks

import com.refer.android.refer9.models.LogInResponseBody
import com.refer.android.refer9.models.LogInRequestBody
import com.refer.android.refer9.models.SignUpRequestBody
import com.refer.android.refer9.models.SignUpResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("login")
     fun loginUser(@Body logInRequestBody: LogInRequestBody) : Call<LogInResponseBody>

    @POST("signup")
    fun signUp(@Body signUpRequestBody: SignUpRequestBody) : Call<SignUpResponseBody>
}