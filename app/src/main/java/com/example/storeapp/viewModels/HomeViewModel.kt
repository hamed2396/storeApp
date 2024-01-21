package com.example.storeapp.viewModels

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.root.logError
import com.example.storeapp.data.repository.home.HomeRepository
import com.example.storeapp.models.home.ResponseBanners
import com.example.storeapp.models.home.ResponseDiscount
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.ProductsCategories
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.network.NetworkStatus.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(300)
            getBanners()
            getDiscount()

        }
    }

    //restore scroll position
    var lastStateOfScroll: Parcelable? = null


    //banner
    private val _bannerData = MutableLiveData<NetworkStatus<ResponseBanners>>()
    val bannerData get() = _bannerData as LiveData<NetworkStatus<ResponseBanners>>
    private fun getBanners() = viewModelScope.launch {
        _bannerData.postValue(Loading())
        val response = repository.getBannersList(Constants.GENERAL)
        _bannerData.postValue(networkResponse.handleResponse(response))
    }

    //discount
    private val _discountData = MutableLiveData<NetworkStatus<ResponseDiscount>>()
    val discountData get() = _discountData as LiveData<NetworkStatus<ResponseDiscount>>
    private fun getDiscount() = viewModelScope.launch {
        _discountData.postValue(Loading())
        val response = repository.getDiscountList(Constants.ELECTRONIC_DEVICES)
        _discountData.postValue(networkResponse.handleResponse(response))
    }

    //products
    private fun productQueries(): HashMap<String, String> {
        val queries = hashMapOf<String, String>()
        queries[Constants.SORT] = Constants.SORT_NEW
        return queries
    }

    private fun callProductsApi(category: ProductsCategories) = liveData {
        val categories = category.category
        emit(Loading())
        val response = repository.getProductList(categories, productQueries())
        emit(networkResponse.handleResponse(response))
    }
    //second way
    /*    val productLiveData= MutableLiveData<NetworkStatus<ResponseProducts>>()
        fun getProduct(category: ProductsCategories)=viewModelScope.launch {
            val categories = category.category
            productLiveData.postValue(Loading())
            val response = repository.getProductList(categories, productQueries())
            productLiveData.postValue(networkResponse.handleResponse(response))

        }*/

    private val categoriesName = ProductsCategories.values().associateWith {

        callProductsApi(it)
        /* second way

          getProduct(it)

          */

    }


    fun getProductsData(category: ProductsCategories) = categoriesName.getValue(category)
}