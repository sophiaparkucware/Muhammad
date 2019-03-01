package com.bandapp.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bandapp.models.Band
import com.bandapp.utilities.ImageLoaderUtils
import kotlinx.android.synthetic.main.single_band.view.*
import android.R.attr.right
import android.R.attr.left
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import com.bandapp.R
import com.bandapp.utilities.Utils.Companion.dpToPx


class BandsAdapter(activity1: Activity, bandsList: ArrayList<Band?>, val clickListener: (Band) -> Unit) :
    RecyclerView.Adapter<BandsAdapter.MyViewHolder>() {
    var bands = bandsList
    var activity = activity1

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val bandImage: ImageView = itemView?.findViewById(com.bandapp.R.id.bandImageSingle)!!
        val bandName: TextView = itemView?.findViewById(R.id.bandNameSingle)!!
        fun bind(band: Band, clickListener: (Band) -> Unit) {
            bandImage.setOnClickListener { clickListener(band) }
        }

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): BandsAdapter.MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(com.bandapp.R.layout.single_band, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bands.size
    }

    override fun onBindViewHolder(p0: BandsAdapter.MyViewHolder, p1: Int) {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        p0.bandImage.layoutParams.width = width / 2
        p0.bandImage.layoutParams.height = width / 2
        p0.bandName.layoutParams.width = width / 2
        p0.bandImage.requestLayout()
        val marginParams = p0.bandImage.getLayoutParams() as MarginLayoutParams
        val marginParams1 = p0.bandName.getLayoutParams() as MarginLayoutParams
        if (p1 % 2 != 0) {
            marginParams.setMargins(dpToPx(13, activity).toInt(), dpToPx(10, activity).toInt(), 0, 0)
            marginParams1.setMargins(dpToPx(13, activity).toInt(), 0, 0, 0)
        } else {
            marginParams.setMargins(0, dpToPx(10, activity).toInt(), dpToPx(13, activity).toInt(), 0)
            marginParams1.setMargins(0, 0, 0, 0)
        }

        val band = bands.get(p1)
        if (band != null) {
            p0.bandName.setText(band.name)
            ImageLoaderUtils.loadImage(band.picture, p0.bandImage, success = { bitmap ->
                p0.bandImage.setImageBitmap(bitmap)
            })
            p0.bind(band, clickListener)
        }
    }

    fun updateList(bandsList: ArrayList<Band?>) {
        this.bands = bandsList
        notifyDataSetChanged()
    }


}