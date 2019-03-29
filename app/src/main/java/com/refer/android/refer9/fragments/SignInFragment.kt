package com.refer.android.refer9.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.refer.android.refer9.R
import com.refer.android.refer9.activities.MainActivity
import com.refer.android.refer9.utils.KeyboardServices
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.SpanStringServices
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign_in.view.*


class SignInFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var auth: FirebaseAuth

    private lateinit var viewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sign_in, container, false)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")


        setSignInText()

        rootView.signIn_fragment_id.setOnClickListener {
            KeyboardServices.hide(requireActivity())
        }

        rootView.signIn_Button.setOnClickListener {
            login()
        }

        rootView.google_sign_in_button.setOnClickListener {
            signInWithGoogle()
        }

        rootView.skip_button.setOnClickListener {
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
            Log.d("TaskLog", "$task")
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("TaskLog", "$account")
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                ToastServices.sToast(requireContext(), "Google Sign In failed ")
                Log.d("TaskLog", "${e.statusCode}")
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
                MySharedPreferences.setPref(requireContext(), "GOOGLE_USER_NAME", user?.displayName)
                MySharedPreferences.setPref(requireContext(),"LOGIN_TYPE_GMAIL",true)
                Log.d("User", "${user?.displayName}")
                onSuccessfulLogin()
            } else {
                Log.d("Temp", "${task.exception}")
            }
        }
    }

    private fun login() {
        KeyboardServices.hide(requireActivity())
        rootView.signIn_Button.startAnimation()
        val email = rootView.signIn_email_box.text.toString().trim { it <= ' ' }
        val password = rootView.signIn_password_box.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            viewModel.signIn(email, password).observe(this, Observer { logInResponse ->
                logInResponse?.let {
                    onSuccessfulLogin()
                    MySharedPreferences.setPref(requireContext(), "USER_TOKEN", it.accessToken)
                }
            })
        } else {
            rootView.signIn_Button.revertAnimation()
            ToastServices.lToast(requireContext(), "Fill Values")
        }
    }

    private fun onSuccessfulLogin() {
        MySharedPreferences.setPref(requireContext(), "LOGIN_STATUS", true)
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
    }

    private fun skip() {
        MySharedPreferences.setPref(requireContext(), "LOGIN_SKIP", true)
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
    }

    private fun setSignInText() {
        val spanString = "No account yet? Create one"
        SpanStringServices.createSpannableString(requireActivity(), rootView.signUp_link_text, spanString, 15, 26, 0)
    }
}
