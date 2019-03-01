package com.bandapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.User
import com.bandapp.utilities.Utils
import kotlinx.android.synthetic.main.activity_login_screen.*

import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R
import android.content.Intent
import android.graphics.Color
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.Serializable


class LoginScreen : BasicAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.bandapp.R.layout.activity_login_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {

    }

    override fun initValuesInViews() {

    }

    override fun setOnViewClickListener() {
        loginBtn?.setOnClickListener {
            if (!ifValidValues()) {
                return@setOnClickListener
            }
            progressBar?.visibility = View.VISIBLE
            val user = User()
            user.email = input_email?.text?.toString()!!
            user.password = input_password?.text?.toString()!!
            user.loginUser(success = { firebaseUser ->
                fetchUserFromDB(firebaseUser)
            }, failure = { s ->
                progressBar?.visibility = View.GONE
                Utils.showSnackbar(progressBar, s!!, Color.RED)
            })
        }


        registerText?.setOnClickListener {
            startActivity(Intent(this@LoginScreen, SignupScreen::class.java))
            finish()
        }
    }

    private fun ifValidValues(): Boolean {
        if (input_email?.text?.toString()?.isEmpty()!!) {
            input_email?.setError("email required")
            return false
        }
        if (input_password?.text?.toString()?.isEmpty()!! || input_password?.text?.toString()?.length!! < 6) {
            input_password?.setError("password too short")
            return false
        }
        return true
    }


    private fun fetchUserFromDB(firebaseUser: FirebaseUser?) {
        val user = User()
        user.id = firebaseUser?.uid
        user.fetchUserFromFirebase(success = {user1 ->
            if (user1 == null){
                user.email = firebaseUser?.email
                val intent = Intent(this@LoginScreen, ProfileScreen::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@LoginScreen, HomeScreen::class.java)
                intent.putExtra("user", user1)
                startActivity(intent)
                finish()
            }
        }, failure = {
                s ->
            progressBar?.visibility = View.GONE
            Utils.showSnackbar(loginBtn, s!!, Color.RED)
        })
    }
}
