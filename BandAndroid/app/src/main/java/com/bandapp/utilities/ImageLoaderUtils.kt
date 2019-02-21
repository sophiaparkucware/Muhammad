package com.bandapp.utilities

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList


// image loading from url
class ImageLoaderUtils {
    companion object {
        var listOfColors = ArrayList<String>()
        private var imageLoader: ImageLoader? = null
        private var options: DisplayImageOptions? = null

        fun loadImage(uri: String?, circleImageView: CircleImageView, success: (Bitmap) -> Unit) {

            if (uri == null) {
                circleImageView.setImageResource(0)
                return
            }
            options = DisplayImageOptions.Builder().considerExifParams(true)
                    .cacheInMemory(false).cacheOnDisk(true).build()
            imageLoader = ImageLoader.getInstance()
            imageLoader!!.handleSlowNetwork(true)
            imageLoader!!.displayImage(uri, circleImageView, options, object : ImageLoadingListener {

                override fun onLoadingCancelled(arg0: String, arg1: View) {
//                    progressBar.setVisibility(View.GONE);
                }

                override fun onLoadingComplete(arg0: String, arg1: View, bitmap: Bitmap) {
//                    progressBar.setVisibility(View.GONE);
                    circleImageView.setImageBitmap(bitmap)
                    success(bitmap)
                }

                override fun onLoadingFailed(arg0: String, arg1: View, arg2: FailReason) {
//                    progressBar.setVisibility(View.GONE);
                }

                override fun onLoadingStarted(arg0: String, arg1: View) {
//                    progressBar.setVisibility(View.GONE);
                }

            })
            return

        }
    }

}