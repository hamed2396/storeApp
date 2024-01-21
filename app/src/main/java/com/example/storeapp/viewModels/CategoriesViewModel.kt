package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.categories.CategoriesRepository
import com.example.storeapp.models.categories.ResponseCategories
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: CategoriesRepository,
    private val networkResponse: NetworkResponse,
) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(300)
            callCategoriesApi()
        }
    }


    private val _categories = MutableLiveData<NetworkStatus<ResponseCategories>>()
    val categoriesData get() = _categories as LiveData<NetworkStatus<ResponseCategories>>
   private fun callCategoriesApi() = viewModelScope.launch {
        _categories.postValue(NetworkStatus.Loading())
        val response = repository.getCategories()
        _categories.postValue(networkResponse.handleResponse(response))
    }


}