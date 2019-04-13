package com.refer.android.refer9.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.HealthRegisterModel
import kotlinx.android.synthetic.main.fragment_lab_register.view.*
import java.util.*


class LabRegisterFragment : Fragment(), OnSelectDateListener {
    private var date: String? = null
    private lateinit var rootView: View

    private lateinit var viewModel: HealthRegisterModel

    override fun onSelectDate(day: Int, month: Int, year: Int) {
        ToastServices.customToastSuccess(requireContext(), "$year")
        date = "$day/$month/$year"
        rootView.edit_text_lab_reg_date.setText(date)
        rootView.edit_text_lab_reg_date.isFocusable = false
        rootView.edit_text_lab_reg_date.isFocusableInTouchMode = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(HealthRegisterModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_lab_register, container, false)

        viewModel.getFragmentTitle().observe(this, Observer { title ->
            rootView.title_fragment_lab.text = title
        })

        rootView.edit_text_lab_reg_date.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val newFragment = DatePickerFragment()
                newFragment.show(childFragmentManager, "datePicker")
            }
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() = LabRegisterFragment()
    }
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private var listener: OnSelectDateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as OnSelectDateListener?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener?.onSelectDate(day, month, year)
    }

}

interface OnSelectDateListener {
    fun onSelectDate(day: Int, month: Int, year: Int)
}
