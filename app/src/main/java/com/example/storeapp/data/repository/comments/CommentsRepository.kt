package com.example.storeapp.data.repository.comments

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class CommentsRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getComments() = api.getComments()
    suspend fun deleteComment(id:Int) = api.deleteComment(id)

}