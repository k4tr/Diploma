package com.example.kelineyt.fragments.shopping

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration (private val spaceHeight: Int) : RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = spaceHeight
        outRect.left = spaceHeight
        outRect.right = spaceHeight
        outRect.bottom = spaceHeight
    }

}