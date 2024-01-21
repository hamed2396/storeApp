package com.example.storeapp.data.repository.cart

import com.example.storeapp.data.network.ApiServices
import com.example.storeapp.models.cart.BodyAddToCart
import com.example.storeapp.models.login.BodyLogin
import javax.inject.Inject

class CartRepository @Inject constructor(private val api: ApiServices) {
    suspend fun postAddToCart(id:Int,bodyAddToCart: BodyAddToCart)=api.postAddToCart(id, bodyAddToCart)
    suspend fun getCartList()=api.getCartList()
    suspend fun getCartContinue()=api.getCartContinue()
    suspend fun putCartIncrement(id:Int)=api.putCartIncrement(id)
    suspend fun putCartDecrement(id:Int)=api.putCartDecrement(id)
    suspend fun deleteCartProduct(id:Int)=api.deleteCartProduct(id)
}