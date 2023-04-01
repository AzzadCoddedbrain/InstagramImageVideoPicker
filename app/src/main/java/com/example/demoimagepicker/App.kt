package com.example.demoimagepicker

import android.app.Application
import android.content.Context
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import com.luck.picture.lib.app.IApp
import com.luck.picture.lib.app.PictureAppMaster
import com.luck.picture.lib.crash.PictureSelectorCrashUtils
import com.luck.picture.lib.engine.PictureSelectorEngine

class App : Application(), IApp, CameraXConfig.Provider {
    override fun onCreate() {
        super.onCreate()
        PictureAppMaster.getInstance().app = this
        PictureSelectorCrashUtils.init { t: Thread?, e: Throwable? -> }
    }

    override fun getAppContext(): Context {
        return this
    }

    override fun getPictureSelectorEngine(): PictureSelectorEngine {
        return PictureSelectorEngineImp()
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    companion object {
        private val TAG = App::class.java.simpleName
    }
}