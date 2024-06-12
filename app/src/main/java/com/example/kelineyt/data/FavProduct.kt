package com.example.kelineyt.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavProduct(
    val product: Product,
    val saveFavIndicator: Int? = null
): Parcelable {
    constructor() : this(Product(),  1)
}
