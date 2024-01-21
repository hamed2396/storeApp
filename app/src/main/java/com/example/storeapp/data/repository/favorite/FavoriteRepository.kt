package com.example.storeapp.data.repository.favorite

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getFavorite() = api.getFavorite()
    suspend fun deleteFavorite(id:Int) = api.deleteFavorite(id)

}