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
import com.example.kelineyt.databinding.CartProductItemBinding
import com.example.kelineyt.databinding.ProductInFavBinding
import com.example.kelineyt.databinding.ProductRvItemBinding
import com.example.kelineyt.helper.getProductPrice
class FavProductAdapter: RecyclerView.Adapter<FavProductAdapter.FavProductsViewHolder>() {
 //////////////это привязка элементов карточки товара.
    inner class FavProductsViewHolder( val binding: ProductInFavBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favProduct: FavProduct) {
            binding.apply {
                Glide.with(itemView).load(favProduct.product.images[0]).into(imgProduct)
                tvName.text = favProduct.product.name

                val priceAfterPercentage = favProduct.product.offerPercentage.getProductPrice(favProduct.product.price)
                tvPrice.text = "${String.format("%.2f", priceAfterPercentage)}₽" }

        }
    }
/////////////
    private val diffCallback = object : DiffUtil.ItemCallback<FavProduct>() {
        override fun areItemsTheSame(oldItem: FavProduct, newItem: FavProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: FavProduct, newItem: FavProduct): Boolean {
            return oldItem == newItem
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavProductAdapter.FavProductsViewHolder {
        return FavProductsViewHolder(
            ProductInFavBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: FavProductAdapter.FavProductsViewHolder, position: Int) {

        val favProduct = differ.currentList[position]
        holder.bind(favProduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(favProduct)
        }
    }
    var onProductClick: ((FavProduct) -> Unit)? = null

}
