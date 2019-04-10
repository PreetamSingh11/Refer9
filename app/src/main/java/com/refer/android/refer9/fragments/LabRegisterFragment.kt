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
import com.refer.android.refer9.utils.ToastServices
import kotlinx.android.synthetic.main.fragment_lab_register.view.*
import java.util.*


class LabRegisterFragment : Fragment(),OnSelectDateListener {
    override fun onSelectDate(year: Int) {
        ToastServices.customToastSuccess(requireContext(),"$year")
    }

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_lab_register, container, false)

        rootView.edit_text_lab_reg_date.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
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
    private var listener : OnSelectDateListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as OnSelectDateListener?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        listener?.onSelectDate(year)
    }

}

interface OnSelectDateListener{
    fun onSelectDate(year: Int)
}
