package com.refer.android.refer9.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.refer.android.refer9.adapters.RecyclerItemClickListener
import com.refer.android.refer9.adapters.ServicesListAdapter
import com.refer.android.refer9.utils.ToastServices
import com.refer.android.refer9.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_services_list.*


class ServicesListFragment : Fragment(){

    private lateinit var rootView: View
    private lateinit var viewModel: LoginViewModel
    private val servicesList: ArrayList<String> = ArrayList()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(com.refer.android.refer9.R.layout.fragment_services_list, container, false)

        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")

        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")


        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")


        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")


        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")


        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")


        servicesList.add("Finance")
        servicesList.add("Health")
        servicesList.add("Grooming")
        servicesList.add("Etc")


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_services_list.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter=ServicesListAdapter(servicesList)
        }

        rv_services_list.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireContext(),
                rv_services_list,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // do whatever
                        ToastServices.customToastSuccess(requireContext(), servicesList[position])

                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // do whatever
                    }
                })
        )
    }

}
