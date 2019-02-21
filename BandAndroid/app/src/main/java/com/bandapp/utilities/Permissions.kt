package com.bandapp.utilities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import com.bandapp.BuildConfig
import java.io.File
import java.io.IOException

class Permissions {
    companion object {
        fun checkStoragePermissions(activity: Activity) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Utils.REQUEST_CODE_WRITE_STORAGE_PERMISION
                        )
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            Utils.REQUEST_CODE_WRITE_STORAGE_PERMISION
                        )
                    }
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (ContextCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        ) {
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                Utils.REQUEST_CODE_WRITE_STORAGE_PERMISION
                            )
                        }
                    }
                } else {
                    checkCameraPermissions(activity)
                }
            }
        }

        // this method is checking camera permissions
        fun checkCameraPermissions(activity: Activity) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.CAMERA
                        )
                    ) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.CAMERA),
                            Utils.REQUEST_CODE_CAMERA_PERMISION
                        )
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.CAMERA),
                            Utils.REQUEST_CODE_CAMERA_PERMISION
                        )
                    }
                }

            } else {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    if (ContextCompat.checkSelfPermission(
                            activity,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                Manifest.permission.CAMERA
                            )
                        ) {
                        } else {
                            // No explanation needed, we can request the permission.
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.CAMERA),
                                Utils.REQUEST_CODE_CAMERA_PERMISION
                            )
                        }
                    }
                } else {
                    openCamera(activity)
                }
            }
        }

        fun openCamera(activity: Activity) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Utils.photoURI = FileProvider.getUriForFile(
                        activity, BuildConfig.APPLICATION_ID,
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.photoURI)
                    activity.startActivityForResult(takePictureIntent, Utils.CAMERA_INTENT)
                }
            }
        }

        var mCurrentPhotoPath: String = ""

        @Throws(IOException::class)
        private fun createImageFile(): File {
            val folder = File(Environment.getExternalStorageDirectory().toString() + "/BandAndroid")
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val images = File(folder.toString() + "/Images")
            if (!images.exists()) {
                images.mkdirs()
            }
            val mFileName = System.currentTimeMillis().toString() + ".jpg"
            val file = File(images, mFileName)
            return file
        }
    }
}