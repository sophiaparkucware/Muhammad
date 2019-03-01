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
        private val DATABASE_MODE = "Development"
        //        val DATABASE_MODE = "Production"
        private val USERS_REFERENCE = "/users"
        private val PROFILE_PICTURES = "profile_pictures_uploads"
        private val BANDS_REFERENCE = "/bands"
        private val BANDS_IMAGES_UPLOADS = "bands_images"
        val database = FirebaseDatabase.getInstance()

        fun getUsersReference(): DatabaseReference {
            return database.getReference(DATABASE_MODE + USERS_REFERENCE)
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

        fun getBandsReference(): DatabaseReference {
            return database.getReference(DATABASE_MODE + BANDS_REFERENCE)
        }

        fun getBandImagesRef(uid: String?): StorageReference {
            return getStorageRef().child(BANDS_IMAGES_UPLOADS).child(uid!!)
        }
    }
}