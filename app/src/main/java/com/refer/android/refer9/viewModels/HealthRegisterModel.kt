package com.refer.android.refer9.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.refer.android.refer9.models.DoctorRegistrationRequestBody
import com.refer.android.refer9.repositories.HealthRegisterRepository

class HealthRegisterModel : ViewModel() {
    private val healthRegisterRepository = HealthRegisterRepository()
    private var fragmentTitle = MutableLiveData<String>()

    fun setFragmentTitle(title: String) {
        fragmentTitle.value = "$title Registration"
    }

    fun getFragmentTitle(): LiveData<String> {
        return fragmentTitle
    }

    fun docRegisterModel(requestBody: DoctorRegistrationRequestBody): LiveData<Int> {
        return healthRegisterRepository.doctorRegistration(requestBody)
    }
}