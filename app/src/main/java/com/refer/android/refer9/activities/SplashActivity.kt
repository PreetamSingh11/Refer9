package com.refer.android.refer9.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.refer.android.refer9.R
import com.refer.android.refer9.models.UserProfile
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.ProfileViewModel

class SplashActivity : AppCompatActivity() {

    private val mWaitHandler = Handler()

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setTheme(R.style.splashScreenTheme)
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        val loginStatus=MySharedPreferences.getPref(this,"LOGIN_STATUS",false)
        val loginSkip=MySharedPreferences.getPref(this,"LOGIN_SKIP",false)
        val token=MySharedPreferences.getPref(this,"USER_TOKEN","NOT available")
        val loginTypeEmail=MySharedPreferences.getPref(this,"LOGIN_TYPE_EMAIL",false)
        val loginTypeGmail=MySharedPreferences.getPref(this,"LOGIN_TYPE_EMAIL",false)


        if (loginStatus!! && loginTypeEmail!!){
            viewModel.userProfile(token!!).observe(this, Observer {userProfileResponse->
                userProfileResponse?.let {
                    openHomeWithEmail(it)
                }
            })
        } else if (loginTypeGmail!!){
            val googleUserName=MySharedPreferences.getPref(this,"GOOGLE_USER_NAME","Hi Anonymous!")
            openHomeWithGoogle(googleUserName)
        } else if (!loginStatus && loginSkip!!){
            openLoginActivity()
        } else{
            ToastServices.customToastError(this,"Something went miserably wrong ")
            Log.d("SplashScreen","Something went miserably wrong")
        }



        mWaitHandler.postDelayed({
            try {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 3000)

    }

    private fun openHomeWithEmail(userProfile: UserProfile) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openHomeWithGoogle(googleUserName: String?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        mWaitHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
