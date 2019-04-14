package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.refer.android.refer9.models.MessageEvent
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.ProfileViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SplashActivity : AppCompatActivity() {

    private val mWaitHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setTheme(com.refer.android.refer9.R.style.splashScreenTheme)
        setContentView(com.refer.android.refer9.R.layout.activity_splash)
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        val loginStatus = MySharedPreferences.getPref(this, "LOGIN_STATUS", false)
        val loginSkip = MySharedPreferences.getPref(this, "LOGIN_SKIP", false)
        val token = MySharedPreferences.getPref(this, "USER_TOKEN", "NOT available")
        val loginTypeEmail = MySharedPreferences.getPref(this, "LOGIN_TYPE_EMAIL", false)
        val loginTypeGmail = MySharedPreferences.getPref(this, "LOGIN_TYPE_GMAIL", false)

        Log.d("Splash", "loginStatus : $loginStatus")
        Log.d("Splash", "loginSkip : $loginSkip")
        Log.d("Splash", "token : $token")
        Log.d("Splash", "loginEmail : $loginTypeEmail")
        Log.d("Splash", "loginGmail : $loginTypeGmail")

        if (loginStatus!! && loginTypeEmail!!) {
            viewModel.userProfile(token!!).observe(this, Observer { userProfileResponse ->
                userProfileResponse?.let {
                    MySharedPreferences.setPref(this, "USER_NAME_EMAIL", it.name)
                    MySharedPreferences.setPref(this, "USER_ID", it.id)
                    openHomeWithEmail()
                }
            })
        } else if (loginTypeGmail!!) {
            openHomeWithGoogle()
        } else if (loginSkip!!) {
            openHome()
        } else if (!loginStatus && !loginSkip) {
            openLoginActivity()
        } else {
            delayFunc()
            openLoginActivity()
        }
    }

    private fun delayFunc() {
        mWaitHandler.postDelayed({
            try {
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                startActivity(intent)
//                finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 3000)
    }

    private fun openHomeWithEmail() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("email", true)
        startActivity(intent)
        finish()
    }

    private fun openHomeWithGoogle() {
        delayFunc()
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("gmail", true)
        startActivity(intent)
        finish()
    }

    private fun openHome() {
        delayFunc()
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun openLoginActivity() {
        delayFunc()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        mWaitHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Suppress("UNUSED")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        ToastServices.customToastError(this,event.message)
    }
}
