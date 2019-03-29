package com.refer.android.refer9.models

data class LogInRequestBody(val email: String, val password: String)

data class LogInResponseBody(val accessToken: String, val tokenType: String)

data class SignUpRequestBody(val name: String, val email: String, val password: String)

data class SignUpResponseBody(val success: Boolean, val message: String)

