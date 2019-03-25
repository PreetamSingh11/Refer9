package com.refer.android.refer9.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.activities.LoginActivity
import com.refer.android.refer9.utils.KeyboardServices
import com.refer.android.refer9.utils.MySharedPreferences
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.utils.ValidationServices
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*


class SignUpFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth

    private var isNameValid: Boolean = false
    private var isEmailValid: Boolean = false
    private var isPasswordValid: Boolean = false

    private var textWatcher: TextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
        override fun afterTextChanged(s: Editable) {
            if (s.toString() == rootView.signUp_password_box.text.toString()){
                rootView.signUp_button.isEnabled = true
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

        rootView.signUp_name_box.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val nameValid =
                    ValidationServices.isNameValidFunc(requireContext(), rootView.signUp_name_box.text.toString())
                when (nameValid.errStatusCode) {
                    101 -> rootView.signUp_name_box.error = nameValid.errMsg
                    102 -> rootView.signUp_name_box.error = nameValid.errMsg
                    else -> isNameValid = true
                }
            }
        }

        rootView.signUp_email_box.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val emailValid =
                    ValidationServices.isEmailValidFunc(requireContext(), rootView.signUp_email_box.text.toString())
                when (emailValid.errStatusCode) {
                    101 -> rootView.signUp_email_box.error = emailValid.errMsg
                    102 -> rootView.signUp_email_box.error = emailValid.errMsg
                    else -> isEmailValid = true
                }
            }
        }

        rootView.signUp_password_box.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val passwordValid = ValidationServices.isPasswordValidFunc(
                    requireContext(),
                    rootView.signUp_password_box.text.toString()
                )
                when (passwordValid.errStatusCode) {
                    101 -> rootView.signUp_password_box.error = passwordValid.errMsg
                    102 -> rootView.signUp_password_box.error = passwordValid.errMsg
                    else -> isPasswordValid = true
                }
            }
        }

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
        val name = signUp_name_box.text.toString()
        val email = signUp_email_box.text.toString().trim { it <= ' ' }
        val password = signUp_password_box.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        ToastServices.lToast(requireContext(), "register call with $name")
                        onSuccess(name)
                    } else {
                        ToastServices.lToast(requireContext(), "register call with ${task.exception}")
                    }
                }
        } else {
            ToastServices.lToast(requireContext(), "Fill Values")
        }
    }

    private fun onSuccess(name: String) {
        MySharedPreferences.setPref(requireContext(), "USER_NAME_EMAIL", name)
        val i = Intent(requireContext(), LoginActivity::class.java)
        startActivity(i)
    }

}
