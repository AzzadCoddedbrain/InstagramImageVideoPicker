package com.example.demoimagepicker

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface OnItemLongClickListener {
    fun onItemLongClick(holder: RecyclerView.ViewHolder?, position: Int, v: View?)
}