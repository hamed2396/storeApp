package com.example.storeapp.data.repository.orders

import com.example.storeapp.data.network.ApiServices
import javax.inject.Inject

class OrdersRepository @Inject constructor(private val api: ApiServices) {
    suspend fun getOrders(status:String) = api.getOrderStatus(status)


}