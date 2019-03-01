package com.bandapp.utilities

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.support.design.widget.Snackbar
import android.view.View
import android.R.string.cancel
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog


class Utils {
    companion object {
        var REQUEST_CODE_WRITE_STORAGE_PERMISION = 105
        val REQUEST_CODE_CAMERA_PERMISION = 106
        val CAMERA_INTENT = 108
        var photoURI: Uri? = null
        fun showSnackbar(view: View?, string: String, color: Int) {
            if (view != null) {
                val snackbar: Snackbar = Snackbar.make(view, string, Snackbar.LENGTH_LONG).setDuration(5000)
                val snackBarView = snackbar.view
                snackbar.setActionTextColor(Color.WHITE)
                snackbar.setAction("CLOSE", object : View.OnClickListener {
                    override fun onClick(p0: View?) {

                    }
                })
                snackBarView.setBackgroundColor(color)
                snackbar.show()
            }
        }

        fun dpToPx(dp: Int, activity: Activity): Float {
            return (dp * activity.resources.getDisplayMetrics().density)
        }

        fun createAlert(context: Context, message: String, event: (Boolean) -> Unit) {
            val builder1 = AlertDialog.Builder(context)
            builder1.setMessage(message)
            builder1.setCancelable(true)

            builder1.setPositiveButton(
                "Accept", { dialog, id ->
                    event(true)
                })

            builder1.setNegativeButton(
                "Decline",
                { dialog, id ->
                    event(false)
                })

            val alert11 = builder1.create()
            alert11.show()
        }
    }
}