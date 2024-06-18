package com.example.kelineyt.adapters
import android.util.Log
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.ProductRvItemBinding
import com.example.kelineyt.helper.getProductPrice
class ProductAdapter(private val onItemClicked: (Product) -> Unit) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    ///
    inner class ProductViewHolder(private val binding: ProductRvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                tvName.text = product.name
                tvPrice.text = product.price.toString()
                Glide.with(itemView).load(product.images[0]).into(imgProduct)
                val priceAfterPercentage = product.offerPercentage.getProductPrice(product.price)
                tvPrice.text = "${String.format("%.2f", priceAfterPercentage)}₽" }
                itemView.setOnClickListener {
                    onItemClicked(product)
                }
            }
        }
    }
class DiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}

