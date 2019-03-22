package com.refer.android.refer9.fragments


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
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
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        rootView.signUp_fragment_id.setOnClickListener{
            KeyboardServices.hide(requireActivity())
        }

        rootView.signUp_button.setOnClickListener{
            register()
        }

        return rootView
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
        MySharedPreferences.setPref(requireContext(),"USER_NAME",name)
        val i = Intent(requireContext(), LoginActivity::class.java)
        startActivity(i)
    }



}
