package com.example.demoimagepicker

import android.content.Context
import com.example.demoimagepicker.ImageCacheUtils.getCacheFileTo3x
import com.example.demoimagepicker.ImageCacheUtils.getCacheFileTo4x
import com.luck.picture.lib.engine.CacheResourcesEngine
import java.io.File

class GlideCacheEngine private constructor() : CacheResourcesEngine {
    override fun onCachePath(context: Context, url: String): String {
        val cacheFile: File? = if (GLIDE_VERSION >= 4) {
            // Glide 4.x
            getCacheFileTo4x(context, url)
        } else {
            // Glide 3.x
            getCacheFileTo3x(context, url)
        }
        return if (cacheFile != null) cacheFile.absolutePath else ""
    }

    companion object {
        private const val GLIDE_VERSION = 4
        private var instance: GlideCacheEngine? = null
        fun createCacheEngine(): GlideCacheEngine? {
            if (null == instance) {
                synchronized(GlideCacheEngine::class.java) {
                    if (null == instance) {
                        instance = GlideCacheEngine()
                    }
                }
            }
            return instance
        }
    }
}