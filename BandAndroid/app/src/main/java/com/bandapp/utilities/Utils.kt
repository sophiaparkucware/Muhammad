package com.bandapp.utilities

import android.graphics.Color
import android.net.Uri
import android.support.design.widget.Snackbar
import android.view.View

class Utils {
    companion object {
        var REQUEST_CODE_WRITE_STORAGE_PERMISION = 105
        val REQUEST_CODE_CAMERA_PERMISION = 106
        val CAMERA_INTENT = 108
        var photoURI: Uri? = null
        fun showSnackbar(view: View?, string: String) {
            if (view != null) {
                val snackbar: Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG).setDuration(5000)
                val snackBarView = snackbar.view
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.setAction("CLOSE", object : View.OnClickListener {
                    override fun onClick(p0: View?) {

                    }
                })
                snackBarView.setBackgroundColor(Color.RED)
                snackbar.show()
            }
        }
    }
}