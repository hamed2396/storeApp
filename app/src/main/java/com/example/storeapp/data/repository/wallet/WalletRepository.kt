package com.example.storeapp.data.repository.wallet

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.ui.wallet.BodyIncreaseWallet
import retrofit2.http.Body
import javax.inject.Inject

class WalletRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getWalletData() = api.getWalletData()
    suspend fun postIncreaseWallet( body: BodyIncreaseWallet)=api.postIncreaseWallet(body)

}