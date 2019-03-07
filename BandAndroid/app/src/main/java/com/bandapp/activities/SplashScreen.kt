package com.bandapp.activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bandapp.R
import com.bandapp.models.User
import com.bandapp.utilities.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        FirebaseAuth.getInstance().signOut()
        val handler = Handler()
        handler.postDelayed({
            val firebaseUser = com.bandapp.database_reference.DatabaseReference.getFirebaseAuth().currentUser
            if (firebaseUser == null) {
                startActivity(Intent(this@SplashScreen, LoginScreen::class.java))
                finish()
            } else {
                fetchUserFromFB(firebaseUser)
            }

        }, 500)
    }

    private fun fetchUserFromFB(firebaseUser: FirebaseUser?) {
        val user = User()
        user.id = firebaseUser?.uid
        user.fetchUserFromFirebase(success = { user1 ->
            if (user1 == null) {
                user.email = firebaseUser?.email
                val intent = Intent(this@SplashScreen, ProfileScreen::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashScreen, HomeScreen::class.java)
                intent.putExtra("user", user1)
                startActivity(intent)
                finish()
            }
        }, failure = { s ->
            Utils.showSnackbar(text, s!!, Color.RED)
        })
    }


}
