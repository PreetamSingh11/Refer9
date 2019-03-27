package com.refer.android.refer9.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import com.refer.android.refer9.activities.LoginActivity
import com.refer.android.refer9.activities.MainActivity
import com.refer.android.refer9.models.ErrorData
import com.refer.android.refer9.utils.KeyboardServices
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.utils.ValidationServices
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*


class SignUpFragment : Fragment(), View.OnFocusChangeListener {

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth

    private var isNameValid: Boolean = false
    private var isEmailValid: Boolean = false
    private var isPasswordValid: Boolean = false

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {
            if (s.toString() == rootView.signUp_password_box.text.toString()) {
                if (isNameValid && isEmailValid && isPasswordValid) {
                    rootView.signUp_button.isEnabled = true
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_sign_up, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        setSignInText()

        rootView.signUp_confirm_password_box.addTextChangedListener(textWatcher)

        rootView.signUp_button.isEnabled = false

        rootView.signUp_fragment_id.setOnClickListener {
            KeyboardServices.hide(requireActivity())
        }

        rootView.signUp_button.setOnClickListener {
            register()
        }

        rootView.signUp_name_box.onFocusChangeListener = this
        rootView.signUp_email_box.onFocusChangeListener = this
        rootView.signUp_password_box.onFocusChangeListener = this

        return rootView
    }

    private fun setSignInText() {
        val ss = SpannableString("Already have an account. Login here")
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                (activity as LoginActivity).addSignInFragment()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpan, 25, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        rootView.login_link_text.text = ss
        rootView.login_link_text.movementMethod = LinkMovementMethod.getInstance()
        rootView.login_link_text.highlightColor = Color.GREEN
    }

    private fun register() {
        KeyboardServices.hide(requireActivity())
        rootView.signUp_button.startAnimation()
        val name = signUp_name_box.text.toString()
        val email = signUp_email_box.text.toString().trim { it <= ' ' }
        val password = signUp_password_box.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Log.d("Register","User name $name")
                        onSuccess(name,email,password)
                    } else {
                        Log.d("Register","Register Errors ${task.exception}")
                        rootView.signUp_button.revertAnimation()
                    }
                    if (task.isComplete){
                        val icon = BitmapFactory.decodeResource(requireContext().resources, R.drawable.ic_check)
                        rootView.signUp_button.doneLoadingAnimation(Color.GREEN,icon)
                    }
                }
        } else {
            rootView.signUp_button.revertAnimation()
            ToastServices.lToast(requireContext(), "Fill Values")
        }
    }

    private fun onSuccess(name: String,email: String,password: String) {
        MySharedPreferences.setPref(requireContext(), "USER_NAME_EMAIL", name)
        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        onSuccessfulLogin("EMAIL")
                    } else {
                        ToastServices.sToast(requireContext(), "Login Failed")
                    }
                }
        } else{
            ToastServices.customToastError(requireContext(),"Something went wrong")
        }
    }

    private fun onSuccessfulLogin(type: String) {
        MySharedPreferences.setPref(requireContext(), "LOGIN_STATUS", true)
        when (type){
            "EMAIL" -> MySharedPreferences.setPref(requireContext(), "LOGIN_TYPE", "EMAIL")
            "GMAIL" -> MySharedPreferences.setPref(requireContext(), "LOGIN_TYPE", "GMAIL")
        }
        val i = Intent(activity, MainActivity::class.java)
        startActivity(i)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            when (v) {
                rootView.signUp_name_box -> {
                    val errorData =
                        ValidationServices.isNameValidFunc(requireContext(), rootView.signUp_name_box.text.toString())
                    if (setErrors(rootView.signUp_name_box_layout, errorData)) {
                        isNameValid = true
                    }
                }
                rootView.signUp_email_box -> {
                    val errorData =
                        ValidationServices.isEmailValidFunc(requireContext(), rootView.signUp_email_box.text.toString())
                    if (setErrors(rootView.signUp_email_box_layout, errorData)) {
                        isEmailValid = true
                    }
                }
                rootView.signUp_password_box -> {
                    val errorData = ValidationServices.isPasswordValidFunc(
                        requireContext(),
                        rootView.signUp_password_box.text.toString()
                    )
                    if (setErrors(rootView.signUp_password_box_layout, errorData)) {
                        isPasswordValid = true
                    }
                }
            }

        }
    }

    private fun setErrors(textEditBoxLayout: TextInputLayout, errorData: ErrorData): Boolean {
        when (errorData.errStatusCode) {
            101 -> textEditBoxLayout.error = errorData.errMsg
            102 -> textEditBoxLayout.error = errorData.errMsg
            else -> {
                textEditBoxLayout.error = null
                return true
            }
        }
        return false
    }

}
