package com.bandapp.utilities

import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.bandapp.R
import com.bandapp.models.Post
import kotlinx.android.synthetic.main.single_post.*

class Dialogs {
    companion object {
        fun editDelPostDialog(activity: Activity, edit: (Boolean) -> Unit) {
            var dialog: AlertDialog? = null
            val view = LayoutInflater.from(activity).inflate(R.layout.cusom_dialog, null)
            val editLayout = view.findViewById(R.id.editLayout) as RelativeLayout
            val deleteLayout = view.findViewById(R.id.deleteLayout) as RelativeLayout
            editLayout.setOnClickListener {
                edit(true)
                if (dialog != null)
                    dialog?.dismiss()
            }
            deleteLayout.setOnClickListener {
                edit(false)
                if (dialog != null)
                    dialog?.dismiss()
            }

            val builder = AlertDialog.Builder(activity)
            builder.setView(view)
            dialog = builder.create()
            dialog?.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation
            dialog.show()
        }

        fun createPopup(post: Post, activity: Activity, bar: View, progressBar: ProgressBar) {
            val popup = PopupMenu(activity, bar)
            popup.menuInflater
                .inflate(com.bandapp.R.menu.popup_menu, popup.menu)
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    post.deletePost { b ->
                        if (b) {
                            Utils.showSnackbar(
                                progressBar,
                                "post deleted",
                                activity.resources.getColor(R.color.colorPrimary)
                            )
                        } else {
                            Utils.showSnackbar(progressBar, "cannot delete post", Color.RED)
                        }
                    }
                    return true
                }
            })
            popup.show()
        }
    }
}