package com.bandapp.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bandapp.R
import com.bandapp.models.Band
import com.bandapp.models.BandGroup

class BandGroupsAdapter(
    activity1: Activity,
    groupsList: ArrayList<BandGroup>?,
    val clickListener: (BandGroup) -> Unit
) :
    RecyclerView.Adapter<BandGroupsAdapter.MyViewHolder>() {
    var groups = groupsList
    var activity = activity1

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textView: TextView = itemView?.findViewById(R.id.groupNameSingle)!!
        fun bind(bandGroup: BandGroup, clickListener: (BandGroup) -> Unit) {
            itemView.findViewById<LinearLayout>(R.id.bandgroup_layout).setOnClickListener { clickListener(bandGroup) }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BandGroupsAdapter.MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.single_group, p0, false)
        return MyViewHolder(view)
    }


    override fun getItemCount(): Int {
        return groups?.size!!
    }

    override fun onBindViewHolder(p0: BandGroupsAdapter.MyViewHolder, p1: Int) {
        val group = groups?.get(p1)
        if (group != null) {
            p0.textView.setText(group.name)
            p0.bind(group, clickListener)
        }
    }

    fun updateList(groupsList: ArrayList<BandGroup>?) {
        this.groups = groupsList
        notifyDataSetChanged()
    }

}