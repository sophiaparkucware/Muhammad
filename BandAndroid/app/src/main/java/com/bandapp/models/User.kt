package com.bandapp.models

import android.graphics.Color
import android.view.View
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.utilities.Utils
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_member_screen.*
import java.io.Serializable

class User : Serializable {
    var id: String? = ""
    var profilePicture: String? = ""
    var email: String? = ""
    var username: String? = ""
    var password: String? = ""
    var userBands: ArrayList<UserBand> = ArrayList()
    var invites: ArrayList<String> = ArrayList()


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

    fun fetchUserFromFirebase(success: (User?) -> Unit, failure: (String?) -> Unit) {
        DatabaseReference.getUsersReference().child(this.id!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    failure("error occured")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val user = p0.getValue(User::class.java)
                        if (user != null) {
                            success(user)
                        }
                    } else {
                        success(null)
                    }
                }

            })
    }

    fun fetchBandsList(success: (ArrayList<Band?>) -> Unit, failure: (String?) -> Unit) {
        DatabaseReference.getBandsReference().orderByChild("createdBy").equalTo(this.id)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    failure("error occured")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.getValue() != null) {
                        val list = ArrayList<Band?>()
                        for (datasnaphot in p0.children) {
                            val band: Band? = datasnaphot.getValue(Band::class.java)
                            if (band != null) {
                                band.bandId = datasnaphot.key
                                list.add(band)
                            }
                        }
                        success(list)
                    } else {
                        success(ArrayList())
                    }
                }
            })
    }

    fun writeUserToFBDB(success: (Boolean?) -> Unit) {
        val map = HashMap<String, String>()
        map.put("email", this.email!!)
        map.put("id", this.id!!)
        map.put("username", this.username!!)
        map.put("profilePicture", this.profilePicture!!)
        DatabaseReference.getUsersReference().child(this.id!!).setValue(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                print("upload success")
                success(true)
            }
        }.addOnFailureListener { exception ->
            print(exception.message)
            success(false)
        }
    }

    fun fetchSearchedUser(username: String?, success: (User?) -> Unit, failure: (String?) -> Unit) {
        DatabaseReference.getUsersReference().orderByChild("username")
            .equalTo(username?.toLowerCase()?.trim())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    failure("error occured")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.getValue() != null) {
                        if (p0.childrenCount > 0) {
                            for (datasnapshot in p0.children) {
                                val user = datasnapshot.getValue(User::class.java)
                                if (user != null) {
                                    success(user)
                                }
                            }
                        } else {
                            success(null)
                        }
                    } else {
                        success(null)
                    }
                }

            })
    }
}

