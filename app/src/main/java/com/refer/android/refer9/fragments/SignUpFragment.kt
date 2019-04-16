package com.refer.android.refer9.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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

    private lateinit var viewModel: LoginViewModel

    private var isServiceProviderChecked = false

    private var isNameValid: Boolean = false
    private var isEmailValid: Boolean = false
    private var isPasswordValid: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Suppress("UNUSED")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        ToastServices.customToastError(requireContext(), event.message)
        rootView.signUp_button.revertAnimation()
    }

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
        rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        setSignInText()


        rootView.domain_spinner.visibility = View.GONE

        rootView.signUp_confirm_password_box.addTextChangedListener(textWatcher)

        rootView.signUp_button.isEnabled = false

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
            object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, domainList) {
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
        var userType = "normal"
        if (isServiceProviderChecked) {
            userType = rootView.domain_spinner.selectedItem.toString()
        }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            viewModel.signUp(name, email, password, userType).observe(this, Observer { signUpResponse ->
                signUpResponse?.let {
                    onSuccessRegister(email, password)
                    val icon = BitmapFactory.decodeResource(
                        requireContext().resources, R.drawable.ic_check
                    )
                    rootView.signUp_button.doneLoadingAnimation(Color.GREEN, icon)
                }
            })
        } else {
            rootView.signUp_button.revertAnimation()
            ToastServices.lToast(requireContext(), "Fill Values")
        }
    }

    private fun onSuccessRegister(email: String, password: String) {
        viewModel.signIn(email, password).observe(this, Observer { logInResponse ->
            logInResponse?.let {
                MySharedPreferences.setPref(requireContext(), "LOGIN_STATUS", true)
                MySharedPreferences.setPref(requireContext(), "USER_TOKEN", it.accessToken)
                MySharedPreferences.setPref(requireContext(), "LOGIN_TYPE_EMAIL", true)
                val i = Intent(activity, MainActivity::class.java)
                i.putExtra("login", true)
                startActivity(i)
            }
        })
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
