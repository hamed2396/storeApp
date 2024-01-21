package com.example.storeapp.data.repository.categories

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class CategoriesProductRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProductCategories(slug:String,queries:Map<String,String>) = api.getProductList(slug, queries)
}