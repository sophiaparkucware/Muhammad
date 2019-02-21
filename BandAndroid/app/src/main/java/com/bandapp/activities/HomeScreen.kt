package com.bandapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bandapp.R
import com.bandapp.models.User

class HomeScreen : BasicAppCompatActivity() {
    var loggedInUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun initValues() {
        loggedInUser = intent?.getSerializableExtra("user") as? User
    }

    override fun initValuesInViews() {

    }

    override fun setOnViewClickListener() {

    }

    override fun initViews() {

    }
}
