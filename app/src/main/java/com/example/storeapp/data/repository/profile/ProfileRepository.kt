package com.example.storeapp.data.repository.profile

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.profile.BodyUpdateProfile
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject

class ProfileRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getProfileData() = api.getProfileData()
    suspend fun postAvatar(@Body postBody: RequestBody)=api.postAvatar(postBody)
    suspend fun putUpdateProfile(body:BodyUpdateProfile)=api.putProfileUpdate(body)
}