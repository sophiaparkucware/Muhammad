package com.bandapp.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bandapp.R
import com.bandapp.models.Band
import com.bandapp.utilities.ImageLoaderUtils
import kotlinx.android.synthetic.main.activity_band_detail_screen.*
import android.support.design.widget.AppBarLayout

import android.view.WindowManager
import android.os.Build

import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import com.bandapp.adapter.BandGroupsAdapter
import com.bandapp.models.BandGroup


class BandDetailScreen : BasicAppCompatActivity() {
    var band: Band? = null
    lateinit var groupsAdapter: BandGroupsAdapter
    var groupsList: ArrayList<BandGroup>? = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.bandapp.R.layout.activity_band_detail_screen)
    }

    override fun initViews() {
    }

    override fun initValues() {
        band = intent.getSerializableExtra("band") as Band?
        groupsAdapter = BandGroupsAdapter(this, groupsList, { bandGroup -> clickedBandGroup(bandGroup) })
        RV_BandGroups?.layoutManager = LinearLayoutManager(this)
        RV_BandGroups?.adapter = groupsAdapter
    }

    override fun initValuesInViews() {
        ImageLoaderUtils.loadImage(band?.picture, band_image, success = {})
        bandName?.setText(band?.name)
        val noOfmembers = band?.membersList?.size
        if (noOfmembers!! > 1) {
            noOfMembers?.setText(noOfmembers.toString() + " Members")
        } else {
            noOfMembers?.setText(noOfmembers.toString() + " Member")
        }
        groupsList = band?.groupsList
        groupsAdapter.updateList(groupsList)
    }

    override fun setOnViewClickListener() {
        backImageBandDetail?.setOnClickListener {
            finish()
        }

        invite_member?.setOnClickListener {
            val intent = Intent(this, AddMemberScreen::class.java)
            intent.putExtra("band", band)
            startActivity(intent)
        }

    }

    fun clickedBandGroup(bandGroup: BandGroup) {
//        val intent = Intent(this, BandDetailScreen::class.java)
//        intent.putExtra("band", band)
//        startActivity(intent)
    }
}
