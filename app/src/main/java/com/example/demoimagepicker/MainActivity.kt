package com.example.demoimagepicker

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.demoimagepicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val mediaList = ArrayList<MediaItem>()

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        for (i in getData()) {
            binding.txtView.append(i.dateModified.toString()+"\n")
            Log.e("TAG", "onCreate: new data is here    ---- >  " + i.path)
        }
    }

    private fun getData() : ArrayList<MediaItem> {
        // Set the projection to retrieve media items
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.MEDIA_TYPE )

        // Set the selection to retrieve only videos and images
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=" + "${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO} OR " +
                        "${MediaStore.Files.FileColumns.MEDIA_TYPE}=" + "${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}"

        // Sort the result by modification date in descending order
        val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"

        // Retrieve the media items from the MediaStore
        val cursor = this.contentResolver.query( MediaStore.Files.getContentUri("external"), projection, selection, null, sortOrder)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                val dateModified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_MODIFIED))
                val mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO ||
                    mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                    val mediaItem = MediaItem(id, path, dateModified)
                    mediaList.add(mediaItem)
                }
            }

            cursor.close()
        }
        return mediaList
    }

}