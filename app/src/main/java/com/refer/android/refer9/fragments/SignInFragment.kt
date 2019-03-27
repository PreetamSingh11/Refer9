package com.refer.android.refer9.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.refer.android.refer9.R
import com.refer.android.refer9.activities.LoginActivity
import com.refer.android.refer9.activities.MainActivity
import com.refer.android.refer9.utils.KeyboardServices
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*


class SignInFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_sign_in, container, false)

        setSignInText()



        rootView.signIn_fragment_id.setOnClickListener{
            KeyboardServices.hide(requireActivity())
        }

        rootView.signIn_Button.setOnClickListener{
            login()
        }

        rootView.google_sign_in_button.setOnClickListener{
            signInWithGoogle()
        }

        rootView.skip_button.setOnClickListener{
            skip()
        }

        auth = FirebaseAuth.getInstance()
        return rootView
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.refer.android.refer9.R.string.default_web_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("TaskLog","$task")
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("TaskLog", "$account")
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                ToastServices.sToast(requireContext(),"Google Sign In failed ")
                Log.d("TaskLog","${e.statusCode}")
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Temp", "firebaseAuthWithGoogle:" + acct.id!!)
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Temp", "signInWithCredential:success")
                    val user = auth.currentUser
                    MySharedPreferences.setPref(requireContext(),"USER_NAME_GMAIL",user?.displayName)
                    Log.d("User","${user?.displayName}")
                    onSuccessfulLogin("GMAIL")
                } else {
                    Log.d("Temp", "${task.exception}")
                }
            }
    }

    private fun login() {
        KeyboardServices.hide(requireActivity())
        val email = signIn_email_box.text.toString().trim { it <= ' ' }
        val password = signIn_password_box.text.toString().trim { it <= ' ' }

        rootView.signIn_Button.startAnimation()
        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Handler().postDelayed({
                            onSuccessfulLogin("EMAIL")
                        },2000)
                    } else {
                        ToastServices.sToast(requireContext(), "Login Failed")
                        rootView.signIn_Button.revertAnimation()
                    }
                    if (task.isComplete){
                        val icon = BitmapFactory.decodeResource(requireContext().resources, R.drawable.ic_check)
                        rootView.signIn_Button.doneLoadingAnimation(Color.GREEN,icon)
                    }
                }
        } else{
            rootView.signIn_Button.revertAnimation()
            ToastServices.lToast(requireContext(),"Fill Values")
        }
    }

    private fun onSuccessfulLogin(type: String) {
        rootView.signIn_page.visibility=View.GONE
        MySharedPreferences.setPref(requireContext(), "LOGIN_STATUS", true)
        when (type){
            "EMAIL" -> MySharedPreferences.setPref(requireContext(), "LOGIN_TYPE", "EMAIL")
            "GMAIL" -> MySharedPreferences.setPref(requireContext(), "LOGIN_TYPE", "GMAIL")
        }

        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
    }

    private fun skip() {
        MySharedPreferences.setPref(requireContext(), "LOGIN_SKIP", true)
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
    }
    private fun setSignInText() {
        val ss = SpannableString("No account yet? Create one")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                (activity as LoginActivity).addSignUpFragment()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 15, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        rootView.signUp_link_text.text = ss
        rootView.signUp_link_text.movementMethod = LinkMovementMethod.getInstance()
        rootView.signUp_link_text.highlightColor= Color.GREEN
    }
}
