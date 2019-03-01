package com.bandapp.activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bandapp.R
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.*
import com.bandapp.utilities.ImageLoaderUtils
import com.bandapp.utilities.Permissions
import com.bandapp.utilities.Utils
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create_band_screen.*
import kotlinx.android.synthetic.main.activity_profile_screen.*
import java.io.ByteArrayOutputStream

class CreateBandScreen : BasicAppCompatActivity() {
    var selectedBandBitmap: Bitmap? = null
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_band_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {
        user = intent?.getSerializableExtra("user") as User?
        groupName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length == 0 || selectedBandBitmap == null) {
                    doneText?.isEnabled = false
                    doneText?.textSize = 14f
                    doneText.setTextColor(Color.parseColor("#99AAAAAA"))
                    return
                }

                doneText?.isEnabled = true
                doneText?.textSize = 17f
                doneText?.setTextColor(Color.BLACK)
            }

        })
    }

    override fun initValuesInViews() {

    }

    override fun setOnViewClickListener() {
        backImageCreateBand?.setOnClickListener {
            finish()
        }

        selectImagelayout?.setOnClickListener {
            Permissions.checkStoragePermissions(this)
        }

        doneText?.setOnClickListener {
            uploadBandImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Utils.CAMERA_INTENT -> {
                if (Utils.photoURI != null) {
                    selectImagelayout?.visibility = View.INVISIBLE
                    ImageLoaderUtils.loadImage(Utils.photoURI?.toString(), bandImage, success = { bitmap ->
                        if (groupName?.text?.length!! > 0) {
                            doneText?.isEnabled = true
                            doneText?.textSize = 17f
                            doneText?.setTextColor(Color.BLACK)
                        }
                        selectImagelayout?.visibility = View.VISIBLE
                        selectedBandBitmap = bitmap
                        bandImage?.setImageBitmap(bitmap)
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
                    Permissions.checkCameraPermissions(this)
                }
            }
            Utils.REQUEST_CODE_CAMERA_PERMISION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.openCamera(this)
                }
            }

        }
    }

    private fun uploadBandImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Creating band");
        progressDialog.show();
        val baos = ByteArrayOutputStream()
        selectedBandBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()
        if (user != null) {
            val bandKey: String? = DatabaseReference.getBandsReference().push().key
            DatabaseReference.getBandImagesRef(bandKey).putBytes(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    DatabaseReference.getBandImagesRef(bandKey)
                        .downloadUrl.addOnSuccessListener { uri ->
                        print(uri)
                        val band = Band()
                        band.createdBy = user?.id
                        band.name = groupName?.text?.toString()
                        band.picture = uri?.toString()!!
                        band.bandCreatedTime = System.currentTimeMillis().toString()
                        band.bandId = bandKey
                        addBandToUser(band)
                        val member = BandMember()
                        member.memberId = user?.id
                        member.profilePicture = user?.profilePicture
                        member.username = user?.username
                        val listOfBandMembers: ArrayList<BandMember> = ArrayList()
                        listOfBandMembers.add(member)
                        band.membersList = listOfBandMembers
                        val bandGroup = BandGroup()
                        bandGroup.name = "General"
                        bandGroup.groupId = DatabaseReference.getBandsReference().push().key
                        val listOfMemberIds: ArrayList<String> = ArrayList()
                        listOfMemberIds.add(member.memberId!!)
                        bandGroup.memberIds = listOfMemberIds
                        val listOfBandGroups: ArrayList<BandGroup> = ArrayList()
                        listOfBandGroups.add(bandGroup)
                        band.groupsList = listOfBandGroups
                        band.sendBandToDB(success = { it ->
                            if (it!!) {
                                finishActivity()
                            } else {
                                progressDialog.dismiss()
                            }

                        })
                    }
                }
            }.addOnFailureListener { exception ->
                progressDialog.dismiss()
                Utils.showSnackbar(continueBtn, exception.message!!, Color.RED)
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun addBandToUser(band: Band) {
        val userBand = UserBand()
        userBand.id = band.bandId
        userBand.isAdmin = true
        if (user != null) {
            var userBandsList = user?.userBands
            if (userBandsList != null) {
                userBandsList.add(userBand)
            } else {
                userBandsList = ArrayList()
                userBandsList.add(userBand)
            }
            user?.userBands = userBandsList
            DatabaseReference.getUsersReference().child(user?.id!!).child("userBands").setValue(userBandsList)
        }
    }

    private fun finishActivity() {
        val intent = Intent()
        intent.putExtra("user", user)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}

