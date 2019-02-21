package com.bandapp.database_reference

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DatabaseReference {
    companion object {
        private val USERS_REFERENCE = "/users"
        private val DATABASE_MODE = "Development"
        //        val DATABASE_MODE = "Production"

        private val PROFILE_PICTURES = "profile_pictures_uploads"
        fun getReference(): DatabaseReference {
            return FirebaseDatabase.getInstance().getReference(DATABASE_MODE + USERS_REFERENCE)
        }

        fun getFirebaseAuth(): FirebaseAuth {
            return FirebaseAuth.getInstance()
        }

        fun getStorageRef(): StorageReference {
            return FirebaseStorage.getInstance().reference
        }

        fun getProfileImagesRef(uid: String?): StorageReference {
            return getStorageRef().child(PROFILE_PICTURES).child(uid!!)
        }
    }
}