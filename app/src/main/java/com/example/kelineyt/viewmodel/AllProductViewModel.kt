package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.CartProduct
import com.example.kelineyt.data.FavProduct
import com.example.kelineyt.data.Product
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
class AllProductViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _allProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val allProducts = _allProducts.asStateFlow()
    init {
        getProducts()
    }
    private fun getProducts() {
        viewModelScope.launch { _allProducts.emit(Resource.Loading()) }
        firestore.collection("Products").get()
            .addOnSuccessListener { result ->
                val allProductsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _allProducts.emit(Resource.Success(allProductsList))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _allProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }


}