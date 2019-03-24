package com.refer.android.refer9.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*


class SignUpFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_sign_up, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        setSignInText()

        rootView.signUp_fragment_id.setOnClickListener{
            KeyboardServices.hide(requireActivity())
        }

        rootView.signUp_button.setOnClickListener{
            register()
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
        rootView.login_link_text.highlightColor= Color.GREEN
    }

    private fun register() {
        KeyboardServices.hide(requireActivity())
        val name = signUp_name_box.text.toString()
        val email = signUp_email_box.text.toString().trim { it <= ' ' }
        val password = signUp_password_box.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    ToastServices.lToast(requireContext(),"register call with $name")
                    onSuccess(name)
                } else{
                    ToastServices.lToast(requireContext(),"register call with ${task.exception}")
                }
            }
        } else {
            ToastServices.lToast(requireContext(),"Fill Values")
        }
    }

    private fun onSuccess(name : String) {
        MySharedPreferences.setPref(requireContext(),"USER_NAME_EMAIL",name)
        val i = Intent(requireContext(), LoginActivity::class.java)
        startActivity(i)
    }
}
