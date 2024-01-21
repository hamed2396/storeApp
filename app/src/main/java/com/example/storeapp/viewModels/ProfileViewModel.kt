package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.profile.ProfileRepository
import com.example.storeapp.models.profile.BodyUpdateProfile
import com.example.storeapp.models.profile.ResponseProfile
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(300)
            callProfileApi()
        }
    }

    private val _profileData = MutableLiveData<NetworkStatus<ResponseProfile>>()
    val profileData get() = _profileData as LiveData<NetworkStatus<ResponseProfile>>
    fun callProfileApi() = viewModelScope.launch {
        _profileData.postValue(NetworkStatus.Loading())
        val response = repository.getProfileData()
        _profileData.postValue(networkResponse.handleResponse(response))
    }

    private val _avatarData = MutableLiveData<NetworkStatus<Unit>>()
    val avatarData get() = _avatarData as LiveData<NetworkStatus<Unit>>
    fun callUploadAvatarApi(body: RequestBody) = viewModelScope.launch {
        _avatarData.postValue(NetworkStatus.Loading())
        val response = repository.postAvatar(body)
        _avatarData.postValue(networkResponse.handleResponse(response))
    }

    private val _updateProfileData = MutableLiveData<NetworkStatus<ResponseProfile>>()
    val updateProfileData get() = _updateProfileData as LiveData<NetworkStatus<ResponseProfile>>
    fun callUpdateProfileApi(body: BodyUpdateProfile) = viewModelScope.launch {
        _updateProfileData.postValue(NetworkStatus.Loading())
        val response = repository.putUpdateProfile(body)
        _updateProfileData.postValue(networkResponse.handleResponse(response))
    }

}