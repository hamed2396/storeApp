package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.orders.OrdersRepository
import com.example.storeapp.models.profile.ResponseOrders
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val repository: OrdersRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _ordersData = MutableLiveData<NetworkStatus<ResponseOrders>>()
    val ordersData get() = _ordersData as LiveData<NetworkStatus<ResponseOrders>>
    fun callOrdersApi(status:String) = viewModelScope.launch {
        _ordersData.postValue(NetworkStatus.Loading())
        val response = repository.getOrders(status)
        _ordersData.postValue(networkResponse.handleResponse(response))
        _ordersData


    }
}