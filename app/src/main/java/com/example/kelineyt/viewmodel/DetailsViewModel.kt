package com.example.kelineyt.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.FavProduct
import com.example.kelineyt.firebase.FirebaseCommon
import com.example.kelineyt.helper.getProductPrice
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    private val _addToFav = MutableStateFlow<Resource<FavProduct>>(Resource.Unspecified())
    val addToCart = _addToCart.asStateFlow()
    val addToFav= _addToFav.asStateFlow()
    private val _isProductInFav = MutableStateFlow<Boolean>(false)
    val isProductInFav = _isProductInFav.asStateFlow()
    private val _favProducts =
        MutableStateFlow<Resource<List<FavProduct>>>(Resource.Unspecified())
    val favProducts = _favProducts.asStateFlow()
    private var favProductDocuments = emptyList<DocumentSnapshot>()
    ///////
    fun isProductInFav(productId: String) {
        firestore.collection("user").document(auth.uid!!).collection("favorite")
            .whereEqualTo("product.id", productId).get()
            .addOnSuccessListener { querySnapshot ->
                _isProductInFav.value = !querySnapshot.isEmpty
            }
            .addOnFailureListener {
                _isProductInFav.value = false
            }
    }


    fun removeProductFromFav(productId: String) {
        viewModelScope.launch { _addToFav.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("favorite")
            .whereEqualTo("product.id", productId).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents.first().id
                    firestore.collection("user").document(auth.uid!!).collection("favorite")
                        .document(documentId).delete()
//                            viewModelScope.launch {
//                                _isProductInFav.emit(false)
//                            }

                }
            }
            .addOnFailureListener { exception ->
                viewModelScope.launch { _addToFav.emit(Resource.Error(exception.message.toString())) }
            }
    }
    fun addUpdateProductInFav(favProduct: FavProduct, i: Int) {
        viewModelScope.launch { _addToFav.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("favorite")

            .whereEqualTo("product.id", favProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        // Товар не найден в избранном, добавляем новый
                        viewModelScope.launch { _addToFav.emit(Resource.Error("Товар добавлен в избранное")) }

                        addNewFavProduct(favProduct)
                    } else {
                        // Товар уже есть в избранном
                        viewModelScope.launch { _addToFav.emit(Resource.Error("Товар уже добавлен в избранное")) }
                    }
                }

            }.addOnFailureListener {
                viewModelScope.launch { _addToFav.emit(Resource.Error(it.message.toString())) }
            }
    }

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) { //Add new product
                        addNewProduct(cartProduct)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product != null) {
                            if(product.product == cartProduct.product && product.selectedColor == cartProduct.selectedColor && product.selectedSize== cartProduct.selectedSize){ //Increase the quantity (fixed quantity increasement issue)
                                val documentId = it.first().id
                                increaseQuantity(documentId, cartProduct)
                            } else { //Add new product
                                addNewProduct(cartProduct)
                            }
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }
            }
    }
    private fun calculatePrice(data: List<FavProduct>): Float {
        return data.sumByDouble { favProduct ->
            (favProduct.product.offerPercentage.getProductPrice(favProduct.product.price)).toDouble()
        }.toFloat()
    }

    val productsPrice = favProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }
    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(addedProduct!!))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }
    private fun addNewFavProduct(favProduct: FavProduct) {
        firebaseCommon.addProductToFav(favProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToFav.emit(Resource.Success(addedProduct!!))
                else
                    _addToFav.emit(Resource.Error(e.message.toString()))
            }
        }
    }
    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCart.emit(Resource.Success(cartProduct))
                else
                    _addToCart.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}










