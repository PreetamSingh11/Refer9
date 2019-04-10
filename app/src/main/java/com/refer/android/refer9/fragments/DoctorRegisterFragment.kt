package com.refer.android.refer9.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.refer.android.refer9.utils.JsonServices
import kotlinx.android.synthetic.main.fragment_doctor_register.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class DoctorRegisterFragment : androidx.fragment.app.Fragment() {

    private lateinit var rootView: View

    private var jsonString: String? = null
    private var listDocType = ArrayList<String>()
    private var listDocSubType = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_doctor_register, container, false)

        jsonString = JsonServices.getJsonFromLocalFile(requireContext(), "doctor.json")
        getDocTypesList()
        populateList(rootView.edit_text_doc_type,listDocType)

        rootView.edit_text_doc_type.onItemClickListener = AdapterView.OnItemClickListener{parent,_,position,_->
            rootView.edit_text_doc_sub_type.text = null
            val selectedItem = parent.getItemAtPosition(position).toString()
            getDocSubTypesList(selectedItem)
            populateList(rootView.edit_text_doc_sub_type,listDocSubType)
        }

        rootView.edit_text_doc_reg_date.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)+1
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener{_,mYear,mMonth,mDay ->
                    val date = "$mDay/$mMonth/$mYear"
                    rootView.edit_text_doc_reg_date.setText(date)
                },year,month,day)
                dpd.show()
            }
        }

        return rootView

    }

    private fun populateList(textView: AutoCompleteTextView, list: ArrayList<String>){
        val listAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,list)
        textView.threshold=0
        textView.setAdapter(listAdapter)
        textView.setOnFocusChangeListener{_,hasFocus ->
            if (hasFocus){
                textView.showDropDown()
            }
        }
    }

    private fun getDocTypesList() {
        try {
            val jsonObject = JSONObject(jsonString)
            val array = jsonObject.getJSONArray("MainMenuItems")
            for (i in 0 until array.length()) {
                val `object` = array.getJSONObject(i)
                val docTypes = `object`.getString("doctor_type")
                listDocType.add(docTypes)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getDocSubTypesList(selectedItem: String) {
        listDocSubType.clear()
        try {
            val jsonObject = JSONObject(jsonString)
            val array = jsonObject.getJSONArray("MainMenuItems")
            for (i in 0 until array.length()) {
                val `object` = array.getJSONObject(i)
                val docTypes = `object`.getString("doctor_type")
                val docSubTypes = `object`.getJSONArray("doctor_sub_type1")
                for (j in 0 until docSubTypes.length()){
                    if (docTypes == selectedItem)
                    listDocSubType.add(docSubTypes[j].toString())
                }

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
