package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.storeapp.data.repository.detail.DetailRepository
import com.example.storeapp.models.SimpleResponse
import com.example.storeapp.models.details.BodyAddComment
import com.example.storeapp.models.details.ResponseChart
import com.example.storeapp.models.details.ResponseDetail
import com.example.storeapp.models.details.ResponseDetailFavorite
import com.example.storeapp.models.details.ResponseFeatures
import com.example.storeapp.models.details.ResponseProductComments
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _detailData = MutableLiveData<NetworkStatus<ResponseDetail>>()
    val detailData get() = _detailData.force<LiveData<NetworkStatus<ResponseDetail>>>()
    fun callDetailApi(id: Int) = viewModelScope.launch {
        _detailData.postValue(NetworkStatus.Loading())
        val response = repository.getDetail(id)
        _detailData.postValue(networkResponse.handleResponse(response))
    }

    private val _favoriteData = MutableLiveData<NetworkStatus<ResponseDetailFavorite>>()
    val favoriteData get() = _favoriteData.force<LiveData<NetworkStatus<ResponseDetailFavorite>>>()
    fun callPostFavoriteApi(id: Int) = viewModelScope.launch {
        _favoriteData.postValue(NetworkStatus.Loading())
        val response = repository.postFavorite(id)
        _favoriteData.postValue(networkResponse.handleResponse(response))
    }


    private val _featuresData = MutableLiveData<NetworkStatus<ResponseFeatures>>()
    val featuresData get() = _featuresData.force<LiveData<NetworkStatus<ResponseFeatures>>>()
    fun callGetFeaturesApi(id: Int) = viewModelScope.launch {
        _featuresData.postValue(NetworkStatus.Loading())
        val response = repository.getFeatures(id)
        _featuresData.postValue(networkResponse.handleResponse(response))
    }

    private val _postCommentData = MutableLiveData<NetworkStatus<SimpleResponse>>()
    val postCommentData get() = _postCommentData.force<LiveData<NetworkStatus<SimpleResponse>>>()
    fun callPostCommentApi(id: Int, bodyAddComment: BodyAddComment) = viewModelScope.launch {
        _postCommentData.postValue(NetworkStatus.Loading())
        val response = repository.postAddComment(id, bodyAddComment)
        _postCommentData.postValue(networkResponse.handleResponse(response))
    }

    private val _getCommentData = MutableLiveData<NetworkStatus<ResponseProductComments>>()
    val getCommentData get() = _getCommentData.force<LiveData<NetworkStatus<ResponseProductComments>>>()
    fun callGetCommentApi(id: Int) = viewModelScope.launch {
        _getCommentData.postValue(NetworkStatus.Loading())
        val response = repository.getProductComments(id)
        _getCommentData.postValue(networkResponse.handleResponse(response))
    }

    private val _getChartData = MutableLiveData<NetworkStatus<ResponseChart>>()
    val getChartData get() = _getChartData.force<LiveData<NetworkStatus<ResponseChart>>>()
    fun callGetChartApi(id: Int) = viewModelScope.launch {
        _getChartData.postValue(NetworkStatus.Loading())
        val response = repository.getProductChart(id)
        _getChartData.postValue(networkResponse.handleResponse(response))
    }


}