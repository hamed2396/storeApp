package com.example.storeapp.data.repository

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.login.BodyLogin
import javax.inject.Inject

class LoginRepository @Inject constructor(private val api: ApiServices) {
    suspend fun postLogin(bodyLogin: BodyLogin) = api.postLogin(bodyLogin)
    suspend fun postVerify(bodyLogin: BodyLogin) = api.postVerify(bodyLogin)
}