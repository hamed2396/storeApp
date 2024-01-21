package com.example.storeapp.data.repository.home

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api:ApiServices) {
suspend fun getBannersList(slug:String)=api.getBannersList(slug)
suspend fun getDiscountList(slug:String)=api.getDiscountList(slug)
suspend fun getProductList(slug:String,queries:Map<String,String>)=api.getProductList(slug,queries)
}