package com.refer.android.refer9.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.refer.android.refer9.models.LogInResponseBody
import com.refer.android.refer9.models.SignUpResponseBody
import com.refer.android.refer9.repositories.LogInRepository

class LoginViewModel : ViewModel() {

    private val logInRepository = LogInRepository()

    fun signIn(email: String, password: String): LiveData<LogInResponseBody> {
        return logInRepository.getSignIn(email, password)
    }

    fun signUp(name: String, email: String, password: String, userType: String): LiveData<SignUpResponseBody> {
        return logInRepository.setSignUp(name, email, password, userType)
    }

}