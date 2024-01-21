package com.example.storeapp.data.repository.detail

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.details.BodyAddComment
import javax.inject.Inject

class DetailRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getDetail(id: Int) = api.getDetail(id)
    suspend fun postFavorite(id: Int) = api.postFavorite(id)
    suspend fun getFeatures(id: Int) = api.getFeatures(id)
    suspend fun postAddComment(id: Int,body:BodyAddComment) = api.postAddComment(id,body)
    suspend fun getProductComments(id: Int) = api.getProductComments(id)
    suspend fun getProductChart(id: Int) = api.getProductChart(id)


}