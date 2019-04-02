package com.refer.android.refer9.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.services_list_items.view.*


class ServicesListAdapter(private val servicesList: ArrayList<String>) :
    RecyclerView.Adapter<ServicesListAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val services = servicesList[position]
        holder.bind(services)
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
        fun bind(service: String) {
            itemView.text_services_list.text = service
        }
    }
}