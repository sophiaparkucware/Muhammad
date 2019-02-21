package com.bandapp.application

import android.app.Application
import android.content.Context
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType

class BandAndroid: Application() {

    override fun onCreate() {
        super.onCreate()
        initImageLoader(this)
    }

    private fun initImageLoader(context: Context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //          ImageLoaderConfiguration.createDefault(this);
        // method.
        val config = ImageLoaderConfiguration.Builder(context)
            .threadPriority(Thread.NORM_PRIORITY - 2)
            .threadPoolSize(2)
            .memoryCache(WeakMemoryCache())
            .denyCacheImageMultipleSizesInMemory()
            .diskCacheFileNameGenerator(Md5FileNameGenerator())
            .diskCacheSize(50 * 1024 * 1024) // 50 Mb
            .tasksProcessingOrder(QueueProcessingType.LIFO)
            .writeDebugLogs() // Remove for release app
            .build()

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config)
    }

}