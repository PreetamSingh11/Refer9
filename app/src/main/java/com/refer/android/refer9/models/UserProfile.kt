package com.refer.android.refer9.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val imageUrl: String? = null,
    val emailVerified: Boolean,
    val provider: String,
    val providerId: String? = null
) : Parcelable