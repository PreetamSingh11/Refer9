package com.refer.android.refer9.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.refer.android.refer9.R
import com.refer.android.refer9.utils.MySharedPreferences
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileUserName.text = MySharedPreferences.getPref(this, "USER_NAME_EMAIL", "Anonymous!!")
    }
}
