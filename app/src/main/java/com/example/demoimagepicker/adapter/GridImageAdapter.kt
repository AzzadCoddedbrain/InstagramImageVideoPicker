package com.example.demoimagepicker.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.demoimagepicker.OnItemLongClickListener
import com.example.demoimagepicker.R
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnItemClickListener
import com.luck.picture.lib.tools.DateUtils
import java.io.File

class GridImageAdapter(val context: Context) : RecyclerView.Adapter<GridImageAdapter.ViewHolder>() {
    val TYPE_CAMERA = 1
    val TYPE_PICTURE = 2
    private var mInflater: LayoutInflater? = null
    private var list: ArrayList<LocalMedia> = ArrayList()
    private var selectMax = 9

    private var mOnAddPicClickListener: onAddPicClickListener? = null

    interface onAddPicClickListener {
        fun onAddPicClick()
    }

    fun delete(position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION && list.size > position) {
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, list.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun GridImageAdapter(
        context: Context,
        mOnAddPicClickListener: GridImageAdapter.onAddPicClickListener
    ) {
        mInflater = LayoutInflater.from(context)
        this.mOnAddPicClickListener = mOnAddPicClickListener
    }


    fun setSelectMax(selectMax: Int) {
        this.selectMax = selectMax
    }

    fun setList(list: List<LocalMedia>) {
        this.list = list as ArrayList<LocalMedia>
    }

    fun getData(): List<LocalMedia> {
        return list ?: java.util.ArrayList()
    }

    fun remove(position: Int) {
        if (list != null && position < list.size) {
            list.removeAt(position)
        }
    }

    class ViewHolder (view: View):RecyclerView.ViewHolder(view){

        var mImg: ImageView = view.findViewById(R.id.fiv)
        var mIvDel: ImageView  = view.findViewById(R.id.iv_del)
        var tvDuration: TextView = view.findViewById(R.id.tv_duration)


    }
    private fun isShowAddItem(position: Int):Boolean{
        val size = if (list.size==0) 0 else list.size
        return position == size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
        if (isShowAddItem(position)){
            return TYPE_CAMERA
        }else{
            return TYPE_PICTURE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.gv_filter_image, parent, false))
    }

    override fun getItemCount(): Int {
        return if (list.size < selectMax) {
            list.size + 1
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(getItemViewType(position)==TYPE_CAMERA){
            viewHolder.mImg.setImageResource(R.drawable.ic_add_image)
            viewHolder.mImg.setOnClickListener { v->mOnAddPicClickListener?.onAddPicClick() }
            viewHolder.mImg.visibility = View.INVISIBLE
        }else{
            viewHolder.mImg.visibility = View.VISIBLE
            viewHolder.mImg.setOnClickListener { v->{
                val index = viewHolder.adapterPosition
                if (index!=RecyclerView.NO_POSITION && list.size > index){
                    list.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(index,list.size)
                } } }
            val media : LocalMedia= list[position]

            if (media==null || !TextUtils.isEmpty(media.path)){
                return
            }

            val chooseModel = media.chooseModel
            var path :String

            if (media.isCut && !media.isCompressed){
                    path = media.cutPath
            }else{
                if (media.isCompressed || media.isCut || media.isCompressed){
                    path = media.cutPath
                }else{
                    if (PictureMimeType.isHasVideo(media.mimeType) && !TextUtils.isEmpty(media.coverPath)){
                        path = media.coverPath
                    }else{
                        path = media.path
                    }
                }
                Log.i("GridImageAdapter.TAG", "::" + media.path)
            }

            if (media.isCut){
                Log.i("GridImageAdapter.TAG", "::" + media.cutPath)
            }
            if (media.isCompressed){
                Log.d("GridImageAdapter.TAG", ":: "+media.compressPath)
                Log.d("GridImageAdapter.TAG", ":: "+File(media.compressPath).length()/1024+"K")
            }

            if (!TextUtils.isEmpty(media.androidQToPath)){
                Log.d("GridImageAdapter.TAG", ":: "+media.androidQToPath)
            }

            if (media.isOriginal){
                Log.i("TAG", "onBindViewHolder: "+media.isOriginal)
                Log.i("TAG", "onBindViewHolder: "+true)
            }

            val duration = media.duration

            viewHolder.tvDuration.visibility = if (PictureMimeType.isHasVideo(media.mimeType)) View.VISIBLE else View.GONE

                if (chooseModel == PictureMimeType.ofAudio()){
                    viewHolder.tvDuration.visibility = View.VISIBLE
                    viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.picture_icon_audio,0,0,0
                    )
                }else{
                    viewHolder.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.picture_icon_video,0,0,0)

                }

            viewHolder.tvDuration.text = DateUtils.formatDurationTime(duration)

            if (chooseModel == PictureMimeType.ofAudio()){
                viewHolder.mImg.setImageResource(R.drawable.picture_audio_placeholder)
            }else{
                Glide.with(viewHolder.itemView.context)
                    .load(
                        if (PictureMimeType.isContent(path) && !media.isCut && !media.isCompressed) Uri.parse(
                            path
                        ) else path
                    )
                    .centerCrop()
                    .placeholder(R.color.app_color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.mImg)
            }
            //itemView
            //itemView
            if (mItemClickListener != null) {
                viewHolder.itemView.setOnClickListener { v: View ->
                    val adapterPosition = viewHolder.adapterPosition
                    mItemClickListener?.onItemClick(v, adapterPosition)
                }
            }

            if (mItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener { v: View ->
                    val adapterPosition = viewHolder.adapterPosition
                    mItemLongClickListener?.onItemLongClick(viewHolder, adapterPosition, v)
                    true
                }
            }


        }
    }

    private var mItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(l: OnItemClickListener?) {
        mItemClickListener = l
    }

    private var mItemLongClickListener: OnItemLongClickListener? = null

    fun setItemLongClickListener(l: OnItemLongClickListener?) {
        mItemLongClickListener = l
    }
}