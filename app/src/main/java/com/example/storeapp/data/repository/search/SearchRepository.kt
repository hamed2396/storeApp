package com.example.storeapp.data.repository.search

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class SearchRepository @Inject constructor(private val api: ApiServices) {
    suspend fun searchProduct(queries: Map<String, String>) = api.searchProduct(queries)
}