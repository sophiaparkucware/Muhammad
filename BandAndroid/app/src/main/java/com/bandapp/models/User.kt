package com.bandapp.models

import android.util.Log
import android.widget.Toast
import com.bandapp.database_reference.DatabaseReference
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.Serializable

class User: Serializable {
    var id: String? = ""
    var profilePicture: String? = ""
    var email: String? = ""
    var username: String? = ""
    var password: String? = ""


    fun signUpUser(success: (FirebaseUser?) -> Unit, failure: (String?) -> Unit) {
        val mAuth = DatabaseReference.getFirebaseAuth()
        mAuth.createUserWithEmailAndPassword(this.email!!, this.password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                success(user)
            } else {
                failure(task.exception?.localizedMessage)
            }
        }
    }

    fun loginUser(success: (FirebaseUser?) -> Unit, failure: (String?) -> Unit) {
        val mAuth = DatabaseReference.getFirebaseAuth()
        mAuth.signInWithEmailAndPassword(this.email!!, this.password!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                success(user)
            } else {
                failure(task.exception?.localizedMessage)
            }
        }
    }

    fun fetchUserFromFirebase(success: (User?) -> Unit, failure: (String?) -> Unit){
        DatabaseReference.getReference().child(this.id!!).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                failure("error occured")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    val user = p0.getValue(User::class.java)
                    if (user != null){
                        success(user)
                    }
                }else{
                    success(null)
                }
            }

        })
    }

    fun writeUserToFBDB(success: (Boolean?) -> Unit){
        val map = HashMap<String, String>()
        map.put("email", this.email!!)
        map.put("id", this.id!!)
        map.put("username", this.username!!)
        map.put("profilePicture", this.profilePicture!!)
        DatabaseReference.getReference().child(this.id!!).setValue(map).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                print("upload success")
                success(true)
            }
        }.addOnFailureListener{
            exception ->
            print(exception.message)
            success(false)
        }
    }
}

