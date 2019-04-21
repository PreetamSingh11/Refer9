package com.refer.android.refer9.models

data class UserProfile(
    val email: String,
    val emailVerified: Boolean,
    val first_name: String,
    val id: Int,
    val imageUrl: String? = null,
    val is_registered: Boolean,
    val name: String,
    val provider: String,
    val providerId: String? = null,
    val user_id: String,
    val user_type: String,
    val username: String
)