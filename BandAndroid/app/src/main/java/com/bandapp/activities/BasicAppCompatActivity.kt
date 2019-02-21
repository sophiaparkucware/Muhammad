package com.bandapp.activities

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log


abstract class BasicAppCompatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initViews()
        initValues()
        setAppBar()
        initValuesInViews()
        setOnViewClickListener()
        setOnViewTouchListener()
        setOnViewItemClickListener()
        setOnViewSwipe()
    }

    fun setOnViewSwipe() {}

    abstract fun initViews()

    abstract fun initValues()

    fun setAppBar() {
        Log.v(TAG, "setAppBar")
    }

    abstract fun initValuesInViews()

    fun setOnViewTouchListener() {
        Log.v(TAG, "setOnViewTouchListener")
    }

    abstract fun setOnViewClickListener()

    fun setOnViewItemClickListener() {
        Log.v(TAG, "setOnViewItemClickListener()")
    }

    companion object {
        private val TAG = "BasicAppCompatActivity"
    }
}