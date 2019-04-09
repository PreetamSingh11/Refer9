package com.refer.android.refer9.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.refer.android.refer9.utils.ToastServices
import kotlinx.android.synthetic.main.services_list_items.view.*


class ServicesListAdapter(private val context: Context,private val servicesList: ArrayList<String>) :
    RecyclerView.Adapter<ServicesListAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val services = servicesList[position]
        holder.bind(context,services)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesListAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ServicesListAdapter.MyViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return servicesList.size
    }

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(com.refer.android.refer9.R.layout.services_list_items, parent, false)) {
        fun bind(context: Context,service: String) {
            itemView.text_services_list.text = service
            itemView.setOnClickListener{
                ToastServices.customToastInfo(context,service)
            }
        }
    }
}