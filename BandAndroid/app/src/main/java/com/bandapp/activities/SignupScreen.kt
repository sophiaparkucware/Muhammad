package com.bandapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.bandapp.R
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.User
import com.bandapp.utilities.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.grpc.okhttp.internal.Util
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
                DatabaseReference.getFirebaseAuth().addAuthStateListener {
                    firebaseAuth ->
                    sendAuthenticationEmail(firebaseAuth.currentUser)
                }
                user.id = firebaseUser?.uid
                Utils.showSnackbar(signupBtn, "An email has been sent to your email address. Please verify your account", resources.getColor(R.color.colorPrimary))
                val handler = Handler()
                handler.postDelayed(Runnable {
                    val intent = Intent(this@SignupScreen, ProfileScreen::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }, 4000)

            }, failure = { s ->
                progressBarSignup?.visibility = View.GONE
                Utils.showSnackbar(progressBarSignup, s!!, Color.RED)
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

    private fun sendAuthenticationEmail(user: FirebaseUser?) {
        user?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(ProfileScreen::class.java.name, "Email sent.")
            }
        }
    }

}
