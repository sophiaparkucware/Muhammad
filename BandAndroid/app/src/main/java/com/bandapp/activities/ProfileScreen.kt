package com.bandapp.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.widget.Toast
import com.bandapp.R
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.User
import com.bandapp.utilities.ImageLoaderUtils
import com.bandapp.utilities.Permissions
import com.bandapp.utilities.Permissions.Companion.checkCameraPermissions
import com.bandapp.utilities.Permissions.Companion.openCamera
import com.bandapp.utilities.Utils
import kotlinx.android.synthetic.main.activity_profile_screen.*
import java.io.ByteArrayOutputStream

class ProfileScreen : BasicAppCompatActivity() {
    var profileImageBitmap: Bitmap? = null
    var loggedInUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {
        loggedInUser = intent?.getSerializableExtra("user") as? User
    }

    override fun initValuesInViews() {

    }

    override fun setOnViewClickListener() {
        capturePhotoLayout?.setOnClickListener {
            Permissions.checkStoragePermissions(this)
        }

        continueBtn?.setOnClickListener {
            if (!ifValidCredentials()) {
                return@setOnClickListener
            }
            val task = DatabaseReference.getFirebaseAuth().currentUser?.reload()
            task?.addOnSuccessListener { void ->
                if (DatabaseReference.getFirebaseAuth().currentUser?.isEmailVerified!!) {
                    uploadProfileImage()
                } else {
                    Utils.showSnackbar(continueBtn, "please verify your email", Color.RED)
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Utils.CAMERA_INTENT -> {
                if (Utils.photoURI != null) {
                    ImageLoaderUtils.loadImage(Utils.photoURI?.toString(), profileImage, success = { bitmap ->
                        profileImageBitmap = bitmap
                    })
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Utils.REQUEST_CODE_WRITE_STORAGE_PERMISION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkCameraPermissions(this)
                }
            }
            Utils.REQUEST_CODE_CAMERA_PERMISION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera(this)
                }
            }

        }
    }

    private fun ifValidCredentials(): Boolean {
        if (profileImageBitmap == null) {
            Toast.makeText(this, "image required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (input_username?.text?.toString()?.isEmpty()!!) {
            input_username?.setError("required")
            return false
        }
        return true
    }

    private fun uploadProfileImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading your profile, please wait.");
        progressDialog.max = 100
        progressDialog.show();
        val baos = ByteArrayOutputStream()
        profileImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()
        if (loggedInUser != null) {
            DatabaseReference.getProfileImagesRef(loggedInUser?.id).putBytes(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    DatabaseReference.getProfileImagesRef(loggedInUser?.id).downloadUrl.addOnSuccessListener { uri ->
                        loggedInUser?.username = input_username?.text?.toString()
                        loggedInUser?.profilePicture = uri?.toString()
                        loggedInUser?.writeUserToFBDB(success = { b ->
                            if (b!!) {
                                val intent = Intent(this@ProfileScreen, HomeScreen::class.java)
                                intent.putExtra("user", loggedInUser)
                                startActivity(intent)
                                finish()
                            } else {
                                progressDialog.dismiss()
                                Utils.showSnackbar(continueBtn, "error occured", Color.RED)
                            }
                        })
                    }

                }
            }.addOnFailureListener { exception ->
                progressDialog.dismiss()
                Utils.showSnackbar(continueBtn, exception.message!!, Color.RED)

            }.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                progressDialog.progress = progress.toInt()
            }
        }
    }
}
