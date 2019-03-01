package com.bandapp.adapter

import android.app.Activity
import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bandapp.R
import com.bandapp.models.Band
import com.bandapp.models.BandGroup
import com.bandapp.utilities.ImageLoaderUtils
import com.bandapp.utilities.Utils.Companion.dpToPx

class InvitesAdapter(
    activity1: Activity,
    bandsList: ArrayList<Band>?,
    val clickListener: (Band) -> Unit
) : RecyclerView.Adapter<InvitesAdapter.MyViewHolder>() {
    var bands = bandsList
    val activity = activity1

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val bandImage: ImageView = itemView?.findViewById(com.bandapp.R.id.bandImageSingle)!!
        val bandName: TextView = itemView?.findViewById(R.id.bandNameSingle)!!
        fun bind(band: Band, clickListener: (Band) -> Unit) {
            bandImage.setOnClickListener { clickListener(band) }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.single_band, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bands?.size!!
    }

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val band = bands?.get(p1)
        p0.bandImage.layoutParams.width = width / 4
        p0.bandImage.layoutParams.height = width / 4
        p0.bandName.layoutParams.width = width / 4
        p0.bandImage.requestLayout()
        p0.bandName.textSize = 12f
        val marginParams = p0.bandImage.getLayoutParams() as ViewGroup.MarginLayoutParams
        val marginParams1 = p0.bandName.getLayoutParams() as ViewGroup.MarginLayoutParams
        if (p1 != 0) {
            marginParams.setMargins(dpToPx(13, activity).toInt(), 0, 0, 0)
            marginParams1.setMargins(dpToPx(13, activity).toInt(), 0, 0, 0)
        }else{
            marginParams.setMargins(0, 0, 0, 0)
            marginParams1.setMargins(0, 0, 0, 0)
        }
        if (band != null) {
            p0.bandName.setText(band.name)
            ImageLoaderUtils.loadImage(band.picture, p0.bandImage, success = { bitmap ->
                p0.bandImage.setImageBitmap(bitmap)
            })
            p0.bind(band, clickListener)
        }
    }

    fun updateList(invitesList: ArrayList<Band>?) {
        this.bands = invitesList
        notifyDataSetChanged()
    }
}