package com.example.kelineyt.viewmodel

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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _favProducts =
        MutableStateFlow<Resource<List<FavProduct>>>(Resource.Unspecified())
    val favProducts = _favProducts.asStateFlow()



    private val _deleteDialog = MutableSharedFlow<FavProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    private var favProductDocuments = emptyList<DocumentSnapshot>()


//    fun deleteCartProduct(favProduct: FavProduct) {
//        val index = favProducts.value.data?.indexOf(favProduct)
//        if (index != null && index != -1) {
//            val documentId = favProductDocuments[index].id
//            firestore.collection("user").document(auth.uid!!).collection("favorite")
//                .document(documentId).delete()
//        }
//    }


//    fun calculatePrice(data: List<FavProduct>): Float {
//        return data.sumByDouble { favProduct ->
//            (favProduct.product.offerPercentage.getProductPrice(favProduct.product.price) * favProduct.quantity).toDouble()
//        }.toFloat()
//    }


    init {
        getFavProducts()
    }


    private fun getFavProducts() {
        viewModelScope.launch { _favProducts.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("favorite")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch { _favProducts.emit(Resource.Error(error?.message.toString())) }
                } else {
                    favProductDocuments = value.documents
                    val favProducts = value.toObjects(FavProduct::class.java)
                    viewModelScope.launch { _favProducts.emit(Resource.Success(favProducts)) }
                }
            }
    }


//    fun changeQuantity(
//        favProduct: FavProduct,
//        quantityChanging: FirebaseCommon.QuantityChanging
//    ) {
//
//        val index = favProducts.value.data?.indexOf(favProduct)
//
//        /**
//         * index could be equal to -1 if the function [getFavProducts] delays which will also delay the result we expect to be inside the [_favProducts]
//         * and to prevent the app from crashing we make a check
//         */
//        if (index != null && index != -1) {
//            val documentId = favProductDocuments[index].id
//            when (quantityChanging) {
//                FirebaseCommon.QuantityChanging.INCREASE -> {
//                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
//                    increaseQuantity(documentId)
//                }
//                FirebaseCommon.QuantityChanging.DECREASE -> {
//                    if (cartProduct.quantity == 1) {
//                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
//                        return
//                    }
//                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
//                    decreaseQuantity(documentId)
//                }
//            }
//        }
//    }

//    private fun decreaseQuantity(documentId: String) {
//        firebaseCommon.decreaseQuantity(documentId) { result, exception ->
//            if (exception != null)
//                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
//        }
//    }

//    private fun increaseQuantity(documentId: String) {
//        firebaseCommon.increaseQuantity(documentId) { result, exception ->
//            if (exception != null)
//                viewModelScope.launch { _cartProducts.emit(Resource.Error(exception.message.toString())) }
//        }
//    }



}