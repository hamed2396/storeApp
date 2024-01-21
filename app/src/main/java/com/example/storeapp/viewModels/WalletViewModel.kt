package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.wallet.WalletRepository
import com.example.storeapp.models.profile.ResponseWallet
import com.example.storeapp.models.wallet.ResponseIncreaseWallet
import com.example.storeapp.ui.wallet.BodyIncreaseWallet
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _walletData = MutableLiveData<NetworkStatus<ResponseWallet>>()
    val walletData get() = _walletData as LiveData<NetworkStatus<ResponseWallet>>
    fun callWalletApi() = viewModelScope.launch {
        _walletData.postValue(NetworkStatus.Loading())
        val response = repository.getWalletData()
        _walletData.postValue(networkResponse.handleResponse(response))
    }

    private val _increaseWallet = MutableLiveData<NetworkStatus<ResponseIncreaseWallet>>()
    val increaseWallet get() = _increaseWallet as LiveData<NetworkStatus<ResponseIncreaseWallet>>
    fun callIncreaseWalletApi(body: BodyIncreaseWallet) = viewModelScope.launch {
        _increaseWallet.postValue(NetworkStatus.Loading())
        val response = repository.postIncreaseWallet(body)
        _increaseWallet.postValue(networkResponse.handleResponse(response))
    }

}