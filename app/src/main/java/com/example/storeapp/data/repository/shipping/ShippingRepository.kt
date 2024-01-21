package com.example.storeapp.data.repository.shipping

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.models.shipping.BodyCheckCoupon
import com.example.storeapp.models.shipping.BodyUpdateAddress
import retrofit2.http.Body
import javax.inject.Inject

class ShippingRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getShipping() = api.getShipment()
    suspend fun putUpdateAddress( body: BodyUpdateAddress)=api.putUpdateAddress(body)
    suspend fun postCheckCoupon(body: BodyCheckCoupon) = api.postCheckCoupon(body)
    suspend fun postPayment(body: BodyCheckCoupon) = api.postPayment(body)

}