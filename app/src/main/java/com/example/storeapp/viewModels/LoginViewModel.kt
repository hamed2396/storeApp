package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.LoginRepository
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.models.login.ResponseLogin
import com.example.storeapp.models.login.ResponseVerify
import com.example.storeapp.utils.Event
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.network.NetworkStatus.Loading
import com.example.storeapp.utils.send
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {
    private val _loginData = MutableLiveData<Event<NetworkStatus<ResponseLogin>>>()
    val loginData get() = _loginData as LiveData<Event<NetworkStatus<ResponseLogin>>>

    fun callLoginApi(bodyLogin: BodyLogin) = viewModelScope.launch {
        _loginData.send(Loading())
        val response = repository.postLogin(bodyLogin)
        _loginData.send(networkResponse.handleResponse(response))
    }

    private val _verifyData = MutableLiveData<NetworkStatus<ResponseVerify>>()
    val verifyData get() = _verifyData as LiveData<NetworkStatus<ResponseVerify>>

    fun callVerifyApi (bodyLogin: BodyLogin) = viewModelScope.launch {
        _verifyData.postValue(Loading())
        val response = repository.postVerify(bodyLogin)
        _verifyData.postValue(networkResponse.handleResponse(response))
    }
}