package com.refer.android.refer9.fragments


import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.refer.android.refer9.R
import com.refer.android.refer9.activities.MainActivity
import com.refer.android.refer9.models.ErrorData
import com.refer.android.refer9.models.MessageEvent
import com.refer.android.refer9.utils.*
import com.refer.android.refer9.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SignUpFragment : Fragment(), View.OnFocusChangeListener {

    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ctx: Context

    private lateinit var viewModel: LoginViewModel

    private var isServiceProviderChecked = false

    private var isNameValid: Boolean = false
    private var isEmailValid: Boolean = false
    private var isPasswordValid: Boolean = false
    private var userType = "normal"

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        EventBus.getDefault().register(this)
        ctx = requireContext()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Suppress("UNUSED")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        ToastServices.customToastError(ctx, event.message)
        rootView.signUp_button.revertAnimation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        setSignInText()

        rootView.domain_spinner.visibility = View.GONE

        rootView.signUp_fragment_id.setOnClickListener {
            KeyboardServices.hide(requireActivity())
        }

        rootView.signUp_button.setOnClickListener {
            register()
        }

        rootView.checkbox_sign_up.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isServiceProviderChecked = true
                rootView.domain_spinner.visibility = View.VISIBLE
            } else {
                isServiceProviderChecked = false
                rootView.domain_spinner.visibility = View.GONE
                rootView.domain_spinner.setSelection(0)
            }
        }

        val domainList = ArrayList<String>()
        domainList.add("Select Domain")
        domainList.add("Education")
        domainList.add("Finance")
        domainList.add("Grooming")
        domainList.add("Health")

        val dataAdapter =
            object : ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_dropdown_item, domainList) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getDropDownView(position, convertView, parent)
                    val textView = view as TextView
                    if (position == 0) {
                        textView.setTextColor(Color.GRAY)
                    } else {
                        textView.setTextColor(Color.BLACK)
                    }
                    return view
                }
            }

        rootView.domain_spinner.adapter = dataAdapter

        rootView.signUp_name_box.onFocusChangeListener = this
        rootView.signUp_email_box.onFocusChangeListener = this
        rootView.signUp_password_box.onFocusChangeListener = this
        rootView.signUp_confirm_password_box.onFocusChangeListener = this

        return rootView
    }

    private fun setSignInText() {
        val spanString = "Already have an account. Login here"
        SpanStringServices.createSpannableString(requireActivity(), rootView.login_link_text, spanString, 25, 35, 1)
    }

    private fun register() {
        KeyboardServices.hide(requireActivity())
        rootView.signUp_button.startAnimation()
        val name = signUp_name_box.text.toString()
        val email = signUp_email_box.text.toString().trim { it <= ' ' }
        val password = signUp_password_box.text.toString().trim { it <= ' ' }
        val confirmPassword = signUp_confirm_password_box.text.toString().trim { it <= ' ' }

        if (isServiceProviderChecked) {
            userType = rootView.domain_spinner.selectedItem.toString().toLowerCase()
        }

        if (validateForm(name, email, password, confirmPassword)) {
            viewModel.signUp(name, email, password, userType).observe(this, Observer { signUpResponse ->
                signUpResponse?.let {
                    onSuccessRegister(email, password)
                    val icon = BitmapFactory.decodeResource(ctx.resources, R.drawable.ic_check)
                    rootView.signUp_button.doneLoadingAnimation(Color.GREEN, icon)
                }
            })
        } else {
            rootView.signUp_button.revertAnimation()
            ToastServices.customToastError(ctx, "Enter correct values")
        }
    }

    private fun validateForm(name: String, email: String, password: String, confirmPassword: String): Boolean {
        val nameErrorData = ValidationServices.isNameValidFunc(ctx, name)
        val emailErrorData = ValidationServices.isEmailValidFunc(ctx, email)
        val passErrorData = ValidationServices.isPasswordValidFunc(ctx, password)

        if (setErrors(rootView.signUp_name_box_layout, nameErrorData)) {
            isNameValid = true
        }
        if (setErrors(rootView.signUp_email_box_layout, emailErrorData)) {
            isEmailValid = true
        }
        if (setErrors(rootView.signUp_password_box_layout, passErrorData)) {
            isPasswordValid = true
        }
        if (password != confirmPassword) {
            rootView.signUp_confirm_password_box_layout.error = "Password doesn't match"
            rootView.signUp_password_box.requestFocus()
            isPasswordValid = false
        }
        return isNameValid && isEmailValid && isPasswordValid
    }

    private fun onSuccessRegister(email: String, password: String) {
        viewModel.signIn(email, password).observe(this, Observer { logInResponse ->
            logInResponse?.let {
                MySharedPreferences.setPref(ctx, "LOGIN_STATUS", true)
                MySharedPreferences.setPref(ctx, "USER_TOKEN", it.accessToken)
                MySharedPreferences.setPref(ctx, "LOGIN_TYPE_EMAIL", true)
                MySharedPreferences.setPref(ctx, "LOGIN_USER_TYPE", userType)
                val i = Intent(activity, MainActivity::class.java)
                i.putExtra("login", true)
                startActivity(i)
            }
        })
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            when (v) {
                rootView.signUp_name_box -> {
                    rootView.signUp_name_box_layout.error = null
                }
                rootView.signUp_email_box -> {
                    rootView.signUp_email_box_layout.error = null
                }
                rootView.signUp_password_box -> {
                    rootView.signUp_password_box_layout.error = null
                }
                rootView.signUp_confirm_password_box -> {
                    rootView.signUp_confirm_password_box_layout.error = null
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
