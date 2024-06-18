package com.example.kelineyt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kelineyt.data.Address
import com.example.kelineyt.util.Constants.USER_COLLECTION
import com.example.kelineyt.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()


    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun saveOrUpdateAddress(address: Address) {
        val validateInputs = validateInputs(address)

        if (validateInputs) {
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }

            val addressCollection = firestore.collection("user").document(auth.uid!!).collection("address")

            if (address.id.isEmpty()) {
                // Create a new address
                addressCollection.document().set(address).addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }
                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                }
            } else {
                // Update existing address
                addressCollection.document(address.id).set(address).addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }
                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                }
            }
        } else {
            viewModelScope.launch {
                _error.emit("Необходимо заполнить все поля!")
            }
        }
    }
    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)

//        if (validateInputs) {
//            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }
//            firestore.collection("user").document(auth.uid!!).collection("address").document()
//                .set(address).addOnSuccessListener {
//                    viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }
//                }.addOnFailureListener {
//                    viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
//                }
//        } else {
//            viewModelScope.launch {
//                _error.emit("Необходимо заполнить все поля!")
//            }
//        }
        if (validateInputs) {
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }
            val documentRef = if (address.id.isEmpty()) {
                firestore.collection("user").document(auth.uid!!).collection("address").document()
            } else {
                firestore.collection("user").document(auth.uid!!).collection("address").document(address.id)
            }

            documentRef.set(address).addOnSuccessListener {
                viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }
            }.addOnFailureListener {
                viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
            }
        } else {
            viewModelScope.launch {
                _error.emit("Необходимо заполнить все поля!")
            }
        }
    }



    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() &&
                address.city.trim().isNotEmpty() &&
                address.phone.trim().isNotEmpty() &&
                address.fullName.trim().isNotEmpty() &&
                address.street.trim().isNotEmpty()
    }

    fun removeAdress(addressTitle: String) {
        viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }
        firestore.collection("user").document(auth.uid!!).collection("address")
            .whereEqualTo("addressTitle", addressTitle).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents.first().id
                    firestore.collection("user").document(auth.uid!!).collection("address")
                        .document(documentId).delete()
                } else {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Error("Address not found")) }
                }
            }
            .addOnFailureListener { exception ->
                viewModelScope.launch { _addNewAddress.emit(Resource.Error(exception.message.toString())) }
            }
    }

}