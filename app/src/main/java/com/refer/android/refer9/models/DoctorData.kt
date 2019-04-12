package com.refer.android.refer9.models

data class DoctorRegistrationRequestBody(
    val doctor_name: String,
    val doctor_registration_no: String,
    val doctor_type: String,
    val doctor_sub_type1: String,
    val doctor_passing_year: String,
    val doctor_college: String,
    val doctor_registration_date: String,
    val user_id: String
)