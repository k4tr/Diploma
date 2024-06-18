package com.example.kelineyt.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.FavProduct
import com.example.kelineyt.data.Product
import com.example.kelineyt.databinding.CartProductItemBinding
import com.example.kelineyt.databinding.ProductInFavBinding
import com.example.kelineyt.databinding.ProductRvItemBinding
import com.example.kelineyt.helper.getProductPrice
class ProductsAdapter: RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
 //////////////это привязка элементов карточки товара.
    inner class ProductsViewHolder( val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

     fun bind(product: Product) {
         binding.apply {
             tvName.text = product.name
             tvPrice.text = product.price.toString()
             Glide.with(itemView).load(product.images[0]).into(imgProduct)
             val priceAfterPercentage = product.offerPercentage.getProductPrice(product.price)
             tvPrice.text = "${String.format("%.2f", priceAfterPercentage)}₽" }
     }
 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapter.ProductsViewHolder {
        return ProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(product)
        }
    }
    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    var onProductClick: ((Product) -> Unit)? = null
}
/////////////



