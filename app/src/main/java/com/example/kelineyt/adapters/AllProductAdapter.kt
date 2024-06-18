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
class AllProductAdapter: RecyclerView.Adapter<AllProductAdapter.AllProductsViewHolder>() {
 //////////////это привязка элементов карточки товара.
    inner class AllProductsViewHolder( val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(allProduct: Product) {
            binding.apply {
                Glide.with(itemView).load(allProduct.images[0]).into(imgProduct)
                tvName.text = allProduct.name

                val priceAfterPercentage = allProduct.offerPercentage.getProductPrice(allProduct.price)
                tvPrice.text = "${String.format("%.2f", priceAfterPercentage)}₽" }

        }
    }
/////////////
    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductAdapter.AllProductsViewHolder {
        return AllProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: AllProductAdapter.AllProductsViewHolder, position: Int) {

        val allProduct = differ.currentList[position]
        holder.bind(allProduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(allProduct)
        }
    }
    var onProductClick: ((Product) -> Unit)? = null

}
