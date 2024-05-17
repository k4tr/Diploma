package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.FavProduct
import com.example.kelineyt.firebase.FirebaseCommon
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {
    private val _addToFav = MutableStateFlow<Resource<FavProduct>>(Resource.Unspecified())

    val addToFav = _addToFav.asStateFlow()
    fun addUpdateProductInFav(favProduct: FavProduct) {
        viewModelScope.launch { _addToFav.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("favourite")
            .whereEqualTo("product.id", favProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {

                    addNewProductInFav(favProduct)

                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToFav.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProductInFav(favProduct: FavProduct) {
        firebaseCommon.addProductToFav(favProduct) { addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _addToFav.emit(Resource.Success(addedProduct!!))
                else
                    _addToFav.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}










