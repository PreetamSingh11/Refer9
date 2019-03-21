package com.refer.android.refer9.activities

import android.os.Bundle
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

class ReferDetailActivity : AppCompatActivity() {

    private var listState = ArrayList<String>()
    private var listCity = ArrayList<String>()

    private var jsonString: String? = null

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

        states_list.onItemClickListener = AdapterView.OnItemClickListener{parent,_,position,_ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
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
}

