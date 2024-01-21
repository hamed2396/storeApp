package com.example.storeapp.data.repository.categories

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getCategories() = api.getCategoriesData()
}