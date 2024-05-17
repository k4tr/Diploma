package com.example.kelineyt.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavProduct(
    val product: Product,
    val description: String? = null,
    val selectedColor: Int? = null,
    val selectedSize: String? = null
): Parcelable {
    constructor() : this(Product(), null,null, null)
}
