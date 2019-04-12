package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.ProfileViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_header.view.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        val intent = intent
        when {
            intent.getBooleanExtra("email", false) -> {
                setUserProfile()
            }
            intent.getBooleanExtra("gmail", false) -> {
                setUserProfile()
            }
            intent.getBooleanExtra("login", false) && MySharedPreferences.getPref(
                this,
                "LOGIN_TYPE_EMAIL",
                false
            )!! -> {
                val token = MySharedPreferences.getPref(this, "USER_TOKEN", "NOT available")
                viewModel.userProfile(token!!).observe(this, Observer { userProfileResponse ->
                    userProfileResponse?.let {
                        MySharedPreferences.setPref(this, "USER_NAME_EMAIL", it.name)
                        MySharedPreferences.setPref(this, "USER_ID", it.id)
                        setUserProfile()
                    }
                })
            }
            else -> setUserProfile()
        }

        menu_icon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.refer_details -> {
                    ToastServices.sToast(this, "Refer Details")
                }
                R.id.about_us -> {
                    ToastServices.sToast(this, "About us")
                }
                R.id.t_and_c -> {
                    ToastServices.sToast(this, "Terms an Conditions")
                }
                R.id.help -> {
                    ToastServices.sToast(this, "Help")
                }
                R.id.signOut -> {
                    FirebaseAuth.getInstance().signOut()
                    MySharedPreferences.setPref(this, "LOGIN_STATUS", false)
                    MySharedPreferences.setPref(this, "LOGIN_TYPE_EMAIL", false)
                    MySharedPreferences.setPref(this, "LOGIN_TYPE_GMAIL", false)
                    MySharedPreferences.setPref(this, "LOGIN_SKIP", false)
                    MySharedPreferences.setPref(this, "USER_TOKEN", " ")
                    setUserProfile()
                }
            }
            true
        }
    }

    private fun setUserProfile() {
        if (MySharedPreferences.getPref(this, "LOGIN_STATUS", false)!!) {
            if (MySharedPreferences.getPref(this, "LOGIN_TYPE_EMAIL", false)!!) {
                val userNameValue = MySharedPreferences.getPref(this, "USER_NAME_EMAIL", "Anonymous!!")
                navigationView.getHeaderView(0).userName.text = userNameValue
            } else {
                val userNameValue = MySharedPreferences.getPref(this, "GOOGLE_USER_NAME", "Anonymous!!!")
                navigationView.getHeaderView(0).userName.text = userNameValue
            }
            navigationView.menu.findItem(R.id.signOut).isEnabled = true
            navigationView.menu.findItem(R.id.refer_details).isEnabled = true
        } else {
            navigationView.getHeaderView(0).userName.text =
                resources.getString(R.string.anonymous)
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
        val intent = Intent(this, HealthActivity::class.java)
        startActivity(intent)
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
