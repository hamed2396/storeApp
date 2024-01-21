package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.address.AddressesRepository
import com.example.storeapp.models.address.BodyAddresses
import com.example.storeapp.models.address.ResponseAddresses
import com.example.storeapp.models.address.ResponseProvince
import com.example.storeapp.models.address.ResponseSubmitAddress
import com.example.storeapp.models.comment.ResponseDelete
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val repository: AddressesRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _addressesData = MutableLiveData<NetworkStatus<ResponseAddresses>>()
    val addressesData get() = _addressesData as LiveData<NetworkStatus<ResponseAddresses>>
    fun callAddressesApi() = viewModelScope.launch {
        _addressesData.postValue(NetworkStatus.Loading())
        val response = repository.getAddresses()
        _addressesData.postValue(networkResponse.handleResponse(response))
    }

    private val _provinceData = MutableLiveData<NetworkStatus<ResponseProvince>>()
    val provinceData get() = _provinceData as LiveData<NetworkStatus<ResponseProvince>>
    fun callProvinceApi() = viewModelScope.launch {
        _provinceData.postValue(NetworkStatus.Loading())
        val response = repository.getProvince()
        _provinceData.postValue(networkResponse.handleResponse(response))
    }

    private val _cityData = MutableLiveData<NetworkStatus<ResponseProvince>>()
    val cityData get() = _cityData as LiveData<NetworkStatus<ResponseProvince>>
    fun callCityApi(id: Int) = viewModelScope.launch {
        _cityData.postValue(NetworkStatus.Loading())
        val response = repository.getCity(id)
        _cityData.postValue(networkResponse.handleResponse(response))
    }

    private val _submitAddressData = MutableLiveData<NetworkStatus<ResponseSubmitAddress>>()
    val submitAddressData get() = _submitAddressData as LiveData<NetworkStatus<ResponseSubmitAddress>>
    fun callSubmitAddressDataApi(bodyAddresses: BodyAddresses) = viewModelScope.launch {
        _submitAddressData.postValue(NetworkStatus.Loading())
        val response = repository.postSubmitAddress(bodyAddresses)
        _submitAddressData.postValue(networkResponse.handleResponse(response))
    }
    private val _deleteAddressData = MutableLiveData<NetworkStatus<ResponseDelete>>()
    val deleteAddressData get() = _deleteAddressData as LiveData<NetworkStatus<ResponseDelete>>
    fun callDeleteAddressDataApi(id:Int) = viewModelScope.launch {
        _deleteAddressData.postValue(NetworkStatus.Loading())
        val response = repository.deleteAddress(id)
        _deleteAddressData.postValue(networkResponse.handleResponse(response))
    }


}