package com.refer.android.refer9.activities

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.refer.android.refer9.fragments.SignInFragment
import com.refer.android.refer9.fragments.SignUpFragment
import com.refer.android.refer9.utils.KeyboardServices
import com.refer.android.refer9.utils.MySharedPreferences
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.refer.android.refer9.R.layout.activity_login)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val loginStatus: Boolean = MySharedPreferences.getPref(this, "LOGIN_STATUS", false)!!

        val vpAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPager.offscreenPageLimit = 2
        viewPager.adapter = vpAdapter

        signInButton.setOnClickListener {
            KeyboardServices.hide(this)
            viewPager.currentItem = 0
        }

        signUpButton.setOnClickListener {
            KeyboardServices.hide(this)
            viewPager.currentItem = 1
        }
        signInButton.isEnabled = false

        if (loginStatus) {
            signUpButton.visibility = View.GONE
            signInButton.visibility= View.GONE
            viewPager.beginFakeDrag()
        }

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) { }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    signUpButton.isEnabled = true
                    signInButton.isEnabled = false
                } else {
                    signInButton.isEnabled = true
                    signUpButton.isEnabled = false
                }
            }
        })
    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return when {
                (position == 0) -> SignInFragment()
                (position == 1) -> SignUpFragment()
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}