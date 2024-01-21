package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.shipping.ShippingRepository
import com.example.storeapp.models.cart.ResponseUpdateCart
import com.example.storeapp.models.shipping.BodyCheckCoupon
import com.example.storeapp.models.shipping.BodyUpdateAddress
import com.example.storeapp.models.shipping.ResponseCheckCoupon
import com.example.storeapp.models.shipping.ResponseShipment
import com.example.storeapp.models.shipping.ResponseUpdateAddress
import com.example.storeapp.models.wallet.ResponseIncreaseWallet
import com.example.storeapp.utils.Event
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.send
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingViewModel @Inject constructor(
    private val repository: ShippingRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _shippingData = MutableLiveData<NetworkStatus<ResponseShipment>>()
    val shippingData get() = _shippingData as LiveData<NetworkStatus<ResponseShipment>>
    fun callShippingApi() = viewModelScope.launch {
        _shippingData.postValue(NetworkStatus.Loading())
        val response = repository.getShipping()
        _shippingData.postValue(networkResponse.handleResponse(response))
    }

    fun callPutUpdateAddressApi(body: BodyUpdateAddress) = viewModelScope.launch {
        repository.putUpdateAddress(body)

    }

    private val _checkCouponData = MutableLiveData<NetworkStatus<ResponseCheckCoupon>>()
    val checkCouponData get() = _checkCouponData as LiveData<NetworkStatus<ResponseCheckCoupon>>
    fun callPostCheckCouponApi(body: BodyCheckCoupon) = viewModelScope.launch {
        _checkCouponData.postValue(NetworkStatus.Loading())
        val response = repository.postCheckCoupon(body)
        _checkCouponData.postValue(networkResponse.handleResponse(response))
    }
    private val _paymentData = MutableLiveData<NetworkStatus<ResponseIncreaseWallet>>()
    val paymentData get() = _paymentData as LiveData<NetworkStatus<ResponseIncreaseWallet >>
    fun callPostPaymentApi(body: BodyCheckCoupon) = viewModelScope.launch {
        _paymentData.postValue(NetworkStatus.Loading())
        val response = repository.postPayment(body)
        _paymentData.postValue(networkResponse.handleResponse(response))
    }


}