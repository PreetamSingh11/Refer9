package com.refer.android.refer9.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.refer.android.refer9.SignInFragment
import com.refer.android.refer9.SignUpFragment
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    //private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.refer.android.refer9.R.layout.activity_login)


        val vpAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit=2
        viewPager.adapter=vpAdapter

        signInButton.setOnClickListener{
            viewPager.currentItem=0
        }

        signUpButton.setOnClickListener{
            viewPager.currentItem=1
        }
        signInButton.isEnabled=false

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                if (position==0){
                    signUpButton.isEnabled=true
                    signInButton.isEnabled=false
                } else{
                    signInButton.isEnabled=true
                    signUpButton.isEnabled=false
                }
            }

        })


//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        val signInFragment = SignInFragment()
//        fragmentTransaction.replace(R.id.viewPager, signInFragment)
//        fragmentTransaction.commit()
//        ToastServices.sToast(this,"Sign In button clicked")
//        signInButton.setOnClickListener{
//
//
//        }

//        activity_login_id.setOnClickListener {
//            KeyboardServices.hide(this)
//        }
//
//        sign_in_button.setOnClickListener {
//            login(it)
//        }
//
//        signUp.setOnClickListener {
//            signUp(it)
//        }
//
//        skip.setOnClickListener {
//            skip(it)
//        }
//
//        login_password.transformationMethod = PasswordTransformationMethod.getInstance()
//
//        firebaseAuth = FirebaseAuth.getInstance()
    }
//
//    @Suppress("UNUSED_PARAMETER")
//    private fun signUp(view: View) {
//        val intent = Intent(this, SignUpActivity::class.java)
//        startActivity(intent)
//    }
//
//    @Suppress("UNUSED_PARAMETER")
//    private fun login(view: View) {
//        KeyboardServices.hide(this)
//        val email = login_email.text.toString().trim { it <= ' ' }
//        val password = login_password.text.toString().trim { it <= ' ' }
//
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(
//                this
//            ) { task ->
//                if (task.isSuccessful) {
//                    onSuccessfulLogin()
//                } else {
//                    ToastServices.sToast(this, "Login Failed")
//                }
//            }
//    }
//
//    private fun onSuccessfulLogin() {
//        MySharedPreferences.setPref(this, "LOGIN_STATUS", true)
//        val i = Intent(this, MainActivity::class.java)
//        startActivity(i)
//    }
//
//    @Suppress("UNUSED_PARAMETER")
//    private fun skip(view: View) {
//        MySharedPreferences.setPref(this, "LOGIN_SKIP", true)
//        val i = Intent(this, MainActivity::class.java)
//        startActivity(i)
//    }

    inner class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            return when{
                (position==0)->SignInFragment()
                (position==1)->SignUpFragment()
                else -> Fragment()
            }
        }
        override fun getCount(): Int {
            return 2
        }
    }
}