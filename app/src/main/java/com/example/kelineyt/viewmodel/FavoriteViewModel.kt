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
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _favProducts = MutableStateFlow<Resource<List<FavProduct>>>(Resource.Unspecified())
    val favProducts = _favProducts.asStateFlow()
    private var favProductDocuments = emptyList<DocumentSnapshot>()
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


}