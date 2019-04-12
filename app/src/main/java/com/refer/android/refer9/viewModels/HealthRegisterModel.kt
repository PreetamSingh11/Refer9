package com.refer.android.refer9.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.refer.android.refer9.models.DoctorRegistrationRequestBody
import com.refer.android.refer9.repositories.HealthRegisterRepository

class HealthRegisterModel: ViewModel() {
    private val healthRegisterRepository = HealthRegisterRepository()

    fun docRegisterModel(requestBody: DoctorRegistrationRequestBody):LiveData<Int>{
        return healthRegisterRepository.doctorRegistration(requestBody)
    }
}