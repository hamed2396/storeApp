package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.cart.CartRepository
import com.example.storeapp.models.SimpleResponse
import com.example.storeapp.models.cart.BodyAddToCart
import com.example.storeapp.models.cart.ResponseUpdateCart
import com.example.storeapp.models.cart.ResponseCart
import com.example.storeapp.utils.Event
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.send
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _addToCart = MutableLiveData<NetworkStatus<SimpleResponse>>()
    val addToCart get() = _addToCart as LiveData<NetworkStatus<SimpleResponse>>
    fun callAddToCartApi(id: Int, body: BodyAddToCart) = viewModelScope.launch {
        _addToCart.postValue(NetworkStatus.Loading())
        val response = repository.postAddToCart(id, body)
        _addToCart.postValue(networkResponse.handleResponse(response))
    }

    private val _cartData = MutableLiveData<NetworkStatus<ResponseCart>>()
    val cartData get() = _cartData as LiveData<NetworkStatus<ResponseCart>>
    fun callGetCartListApi() = viewModelScope.launch {
        _cartData.postValue(NetworkStatus.Loading())
        val response = repository.getCartList()
        _cartData.postValue(networkResponse.handleResponse(response))
    }

    private val _cartUpdateData = MutableLiveData<NetworkStatus<ResponseUpdateCart>>()
    val cartUpdateData get() = _cartUpdateData as LiveData<NetworkStatus<ResponseUpdateCart>>
    fun callPutCartIncrementDataApi(id: Int) = viewModelScope.launch {
        _cartUpdateData.postValue(NetworkStatus.Loading())
        val response = repository.putCartIncrement(id)
        _cartUpdateData.postValue(networkResponse.handleResponse(response))
    }


    fun callPutCartDecrementDataApi(id: Int) = viewModelScope.launch {
        _cartUpdateData.postValue(NetworkStatus.Loading())
        val response = repository.putCartDecrement(id)
        _cartUpdateData.postValue(networkResponse.handleResponse(response))
    }


    fun callDeleteProductDataApi(id: Int) = viewModelScope.launch {
        _cartUpdateData.postValue(NetworkStatus.Loading())
        val response = repository.deleteCartProduct(id)
        _cartUpdateData.postValue(networkResponse.handleResponse(response))
    }


    private val _cartContinueData = MutableLiveData<Event<NetworkStatus<ResponseUpdateCart>>>()
    val cartContinueData get() = _cartContinueData as LiveData<Event<NetworkStatus<ResponseUpdateCart>>>
    fun callGetCartContinueApi() = viewModelScope.launch {
        _cartContinueData.send(NetworkStatus.Loading())
        val response = repository.getCartContinue()
        _cartContinueData.send(networkResponse.handleResponse(response))
    }



}