package com.example.demoimagepicker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.demoimagepicker.adapter.GridImageAdapter
import com.example.demoimagepicker.databinding.ActivityMainBinding
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.instagram.InsGallery

class MainActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    val mAdapter : GridImageAdapter?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btn.setOnClickListener(){
            InsGallery.openGallery(
                this@MainActivity,
                GlideEngine.createGlideEngine(),
                GlideCacheEngine.createCacheEngine(),
                mAdapter?.data
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                PictureConfig.CHOOSE_REQUEST ->{
                    val selectList = PictureSelector.obtainMultipleResult(data)


                    for (media in selectList) {
                        Log.i("MainActivity.TAG", "path :" + media.isCompressed)
                        Log.i("MainActivity.TAG", "comress path:" + media.compressPath)
                        Log.i("MainActivity.TAG", "path:" + media.path)
                        Log.i("MainActivity.TAG", "cut path:" + media.isCut)
                        Log.i("MainActivity.TAG", "path:" + media.cutPath)
                        Log.i("MainActivity.TAG", "path original:" + media.isOriginal)
                        Log.i("MainActivity.TAG", "原图路径:" + media.originalPath)
                        Log.i("MainActivity.TAG", "Android Q 特有Path:" + media.androidQToPath)
                        Log.i("MainActivity.TAG", "Size: " + media.size)
                    }
                    Log.e("TAG", "onActivityResult: -- > "+selectList.toList())
                    mAdapter?.setList(selectList)
                    mAdapter?.notifyDataSetChanged()

                }
            }
        }
    }


}