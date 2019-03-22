package com.refer.android.refer9.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.refer.android.refer9.R
import kotlinx.android.synthetic.main.activity_refer_detail.*
import org.json.JSONException
import org.json.JSONObject
import com.refer.android.refer9.utils.ArrayListServices
import com.refer.android.refer9.utils.JsonServices
import com.refer.android.refer9.utils.KeyboardServices
import com.refer.android.refer9.utils.ToastServices

class ReferDetailActivity : AppCompatActivity() {

    private var listState = ArrayList<String>()
    private var listCity = ArrayList<String>()

    private var jsonString: String? = null

    private lateinit var selectedLoan : String
    private lateinit var selectedBank : String
    private lateinit var selectedState : String
    private lateinit var selectedCity : String
    private lateinit var refereeName : String
    private lateinit var refereePhoneNo : String
    private lateinit var refereeEmail : String
    private lateinit var refereeOccupation : String
    private lateinit var refereeAmount : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_detail)

        refer_activity_id.setOnClickListener{
            KeyboardServices.hide(this)
        }

        val categories = ArrayList<String>()
        categories.add("Personal Loan")
        categories.add("Home Loan")
        categories.add("Car/Bike Loan")
        categories.add("Education Loan")
        categories.add("Business Loan")

        val banks = ArrayList<String>()
        banks.add("Axis")
        banks.add("Bank of Baroda")
        banks.add("HDFC")
        banks.add("ICICI")
        banks.add("State Bank of India")

        jsonString = JsonServices.getJsonFromLocalFile(this, "cities.json")
        getStatesList(jsonString)

        val removedDuplicatedList = ArrayListServices.removeDuplicates(listState)
        val stateList = ArrayListServices.sortAsc(removedDuplicatedList)

        populateList(loan_types,categories)
        populateList(bank_names,banks)
        populateList(states_list,stateList)

        button_refer_detail_submit.setOnClickListener{
            submitDetails()
        }

        states_list.onItemClickListener = AdapterView.OnItemClickListener{parent,_,position,_ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            ToastServices.customToast(this,selectedItem)
            val sortedCities = ArrayListServices.sortAsc(getCitiesList(selectedItem))
            populateList(cities_names,sortedCities)
        }

    }

    private fun getCitiesList(stateSelected: String): ArrayList<String> {
        listCity.clear()
        try {
            val jsonObject = JSONObject(jsonString)
            val array = jsonObject.getJSONArray("array")
            for (i in 0 until array.length()) {
                val `object` = array.getJSONObject(i)
                val state = `object`.getString("state")
                val city = `object`.getString("name")
                if (state == stateSelected) {
                    listCity.add(city)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return listCity
    }

    private fun getStatesList(json: String?) {
        try {
            val jsonObject = JSONObject(json)
            val array = jsonObject.getJSONArray("array")
            for (i in 0 until array.length()) {
                val `object` = array.getJSONObject(i)
                val state = `object`.getString("state")
                listState.add(state)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun populateList(textView: AutoCompleteTextView,list: ArrayList<String>){
        val loanAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        textView.threshold=0
        textView.setAdapter(loanAdapter)
        textView.setOnFocusChangeListener{_,hasFocus ->
            if (hasFocus){
                textView.showDropDown()
            }
        }
    }

    private fun submitDetails(){

        selectedLoan = loan_types.text.toString()
        selectedBank = bank_names.text.toString()
        selectedState = states_list.text.toString()
        selectedCity = cities_names.text.toString()
        refereeName = edit_text_name.text.toString()
        refereePhoneNo = edit_text_mobileNumber.text.toString()
        refereeEmail = edit_text_email.text.toString()
        refereeOccupation = edit_text_occupation.text.toString()
        refereeAmount = edit_text_amount.text.toString()

        if (!TextUtils.isEmpty(selectedLoan) && !TextUtils.isEmpty(selectedBank) &&
                !TextUtils.isEmpty(selectedState) && !TextUtils.isEmpty(selectedCity) &&
                !TextUtils.isEmpty(refereeName) && !TextUtils.isEmpty(refereePhoneNo)) {

            val intent = Intent(this,ConfirmActivity::class.java)
            intent.putExtra("loanType",selectedLoan)
            intent.putExtra("bank",selectedBank)
            intent.putExtra("state",selectedState)
            intent.putExtra("city",selectedCity)
            intent.putExtra("name",refereeName)
            intent.putExtra("phone",refereePhoneNo)
            intent.putExtra("email",refereeEmail)
            intent.putExtra("occupation",refereeOccupation)
            intent.putExtra("amount",refereeAmount)
            startActivity(intent)
        } else {
            ToastServices.customToast(this,"Complete Details")
        }
    }
}

