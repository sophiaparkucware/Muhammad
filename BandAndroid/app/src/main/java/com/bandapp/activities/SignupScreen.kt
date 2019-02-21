package com.bandapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bandapp.R
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.User
import com.bandapp.utilities.Utils
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login_screen.*
import kotlinx.android.synthetic.main.activity_signup_screen.*

class SignupScreen : BasicAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {

    }

    override fun initValuesInViews() {

    }

    override fun setOnViewClickListener() {
        signupBtn?.setOnClickListener {
            if (!ifValidValues()) {
                return@setOnClickListener
            }
            progressBarSignup?.visibility = View.VISIBLE
            val user = User()
            user.email = input_email_signup?.text?.toString()!!
            user.password = input_password_signup?.text?.toString()!!
            user.signUpUser(success = { firebaseUser ->
                user.id = firebaseUser?.uid
                sendAuthenticationEmail(firebaseUser)
                val intent = Intent(this@SignupScreen, ProfileScreen::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }, failure = { s ->
                progressBarSignup?.visibility = View.GONE
                Utils.showSnackbar(progressBarSignup, s!!)
            })
        }
    }

    private fun ifValidValues(): Boolean {
        if (input_email_signup?.text?.toString()?.isEmpty()!!) {
            input_email_signup?.setError("email required")
            return false
        }
        if (input_password_signup?.text?.toString()?.isEmpty()!! || input_password_signup?.text?.toString()?.length!! < 6) {
            input_password_signup?.setError("password too short")
            return false
        }
        return true
    }

    private fun sendAuthenticationEmail(user: FirebaseUser?){
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("", "Email sent.")
            }
        }
    }

}
