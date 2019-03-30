package com.refer.android.refer9.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.refer.android.refer9.models.UserProfile
import com.refer.android.refer9.repositories.ProfileRepository

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepository()

    fun userProfile(token:String):LiveData<UserProfile>{
        return profileRepository.getProfile(token)
    }
}