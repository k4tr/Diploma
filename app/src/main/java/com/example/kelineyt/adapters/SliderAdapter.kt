package com.example.kelineyt.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.example.kelineyt.R
import com.example.kelineyt.data.SliderModel
import com.example.kelineyt.databinding.SliderViewpagerBinding


class SliderAdapter: RecyclerView.Adapter<SliderAdapter.SliderAdapterHolder>(){
    inner class SliderAdapterHolder(val binding: SliderViewpagerBinding): ViewHolder(binding.root){
        fun bind(imagePath: String){
            Glide.with(itemView).load(imagePath).into(binding.idSliderViewPager)
        }
    }
    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderAdapterHolder {
        return SliderAdapterHolder(
            SliderViewpagerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SliderAdapterHolder, position: Int) {
        val image = differ.currentList[position]
        holder.bind(image)
    }

}