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

                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 原图path
                    // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                    // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                    for (media in selectList) {
                        Log.i("MainActivity.TAG", "是否压缩:" + media.isCompressed)
                        Log.i("MainActivity.TAG", "压缩:" + media.compressPath)
                        Log.i("MainActivity.TAG", "原图:" + media.path)
                        Log.i("MainActivity.TAG", "是否裁剪:" + media.isCut)
                        Log.i("MainActivity.TAG", "裁剪:" + media.cutPath)
                        Log.i("MainActivity.TAG", "是否开启原图:" + media.isOriginal)
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