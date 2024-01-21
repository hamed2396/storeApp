package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.favorite.FavoriteRepository
import com.example.storeapp.models.comment.ResponseDelete
import com.example.storeapp.models.favorite.ResponseFavorite
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _favoriteData = MutableLiveData<NetworkStatus<ResponseFavorite>>()
    val favoriteData get() = _favoriteData as LiveData<NetworkStatus<ResponseFavorite>>
    fun callFavoriteApi() = viewModelScope.launch {
        _favoriteData.postValue(NetworkStatus.Loading())
        val response = repository.getFavorite()
        _favoriteData.postValue(networkResponse.handleResponse(response))
    }

    private val _favoriteDeleteData = MutableLiveData<NetworkStatus<ResponseDelete>>()
    val favoriteDeleteData get() = _favoriteDeleteData as LiveData<NetworkStatus<ResponseDelete>>
    fun callDeleteFavoriteApi(id:Int) = viewModelScope.launch {
        _favoriteDeleteData.postValue(NetworkStatus.Loading())
        val response = repository.deleteFavorite(id)
        _favoriteDeleteData.postValue(networkResponse.handleResponse(response))
    }

}