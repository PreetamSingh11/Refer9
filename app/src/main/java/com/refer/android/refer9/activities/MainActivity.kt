package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.navigation_header.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(com.refer.android.refer9.R.layout.activity_main)

        getLoginStatus()
        setUserProfile()

        menu_icon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                com.refer.android.refer9.R.id.refer_details -> {
                    ToastServices.sToast(this, "Refer Details")
                }
                com.refer.android.refer9.R.id.about_us -> {
                    ToastServices.sToast(this, "About us")
                }
                com.refer.android.refer9.R.id.t_and_c -> {
                    ToastServices.sToast(this, "Terms an Conditions")
                }
                com.refer.android.refer9.R.id.help -> {
                    ToastServices.sToast(this, "Help")
                }
                com.refer.android.refer9.R.id.signOut -> {
                    FirebaseAuth.getInstance().signOut()
                    MySharedPreferences.setPref(this, "LOGIN_STATUS", false)
                    setUserProfile()
                }
            }
            true
        }
    }


    private fun getLoginStatus() {
        val loginStatus = MySharedPreferences.getPref(this, "LOGIN_STATUS", false)
        val loginSkip = MySharedPreferences.getPref(this, "LOGIN_SKIP", false)
        if (!loginStatus!! && !loginSkip!!) {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }
    }

    private fun setUserProfile() {
        if (MySharedPreferences.getPref(this, "LOGIN_STATUS", false)!!) {
            if ("EMAIL" == MySharedPreferences.getPref(this, "LOGIN_TYPE", "EMAIL")) {
                val userNameValue = MySharedPreferences.getPref(this, "USER_NAME_EMAIL", "Anonymous!")
                navigationView.getHeaderView(0).userName.text = userNameValue
            } else {
                val userNameValue = MySharedPreferences.getPref(this, "USER_NAME_GMAIL", "Anonymous!")
                navigationView.getHeaderView(0).userName.text = userNameValue
            }
            navigationView.menu.findItem(R.id.signOut).isEnabled = true
            navigationView.menu.findItem(R.id.refer_details).isEnabled = true
        } else {
            navigationView.getHeaderView(0).userName.text = resources.getString(R.string.anonymous)
            navigationView.menu.findItem(R.id.signOut).isEnabled = false
            navigationView.menu.findItem(R.id.refer_details).isEnabled = false
            navigationView.getHeaderView(0).userProfile.setOnClickListener {
                login()
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun login() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun openFinance(view: View) {
        val intent = Intent(this, FinanceActivity::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun openHealth(view: View) {
    }

    @Suppress("UNUSED_PARAMETER")
    fun openOthers(view: View) {
    }

    @Suppress("UNUSED_PARAMETER")
    fun openGrooming(view: View) {
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
