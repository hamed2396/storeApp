package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.comments.CommentsRepository
import com.example.storeapp.models.comment.ResponseComments
import com.example.storeapp.models.comment.ResponseDelete
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val repository: CommentsRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {

    private val _commentsData = MutableLiveData<NetworkStatus<ResponseComments>>()
    val commentsData get() = _commentsData as LiveData<NetworkStatus<ResponseComments>>
    fun callGetCommentsApi() = viewModelScope.launch {
        _commentsData.postValue(NetworkStatus.Loading())
        val response = repository.getComments()
        _commentsData.postValue(networkResponse.handleResponse(response))
    }
    private val _deleteCommentsData = MutableLiveData<NetworkStatus<ResponseDelete>>()
    val deleteCommentsData get() = _deleteCommentsData as LiveData<NetworkStatus<ResponseDelete>>
    fun callDeleteCommentsApi(id:Int) = viewModelScope.launch {
        _deleteCommentsData.postValue(NetworkStatus.Loading())
        val response = repository.deleteComment(id)
        _deleteCommentsData.postValue(networkResponse.handleResponse(response))
    }

}