package com.example.storeapp.models.home


import com.google.gson.annotations.SerializedName

class ResponseDiscount : ArrayList<ResponseDiscount.ResponseDiscountItem>(){
    data class ResponseDiscountItem(
        @SerializedName("discount_rate")
        val discountRate: String?, // 5
        @SerializedName("discounted_price")
        val discountedPrice: String?, // 125000
        @SerializedName("end_time")
        val endTime: String?, // 2024-03-19T09:37:00.000000Z
        @SerializedName("final_price")
        val finalPrice: Int?, // 2375000
        @SerializedName("id")
        val id: Int?, // 53
        @SerializedName("image")
        val image: String?, // /storage/cache/400-0-0-width-BLm3XdhrYfUKQNoBXgFU9xFejqWme0dyMxPRBs6P.jpg
        @SerializedName("product_price")
        val productPrice: String?, // 2500000
        @SerializedName("quantity")
        val quantity: String?, // 2
        @SerializedName("status")
        val status: String?, // discount
        @SerializedName("title")
        val title: String? // شارژر بی سیم فون مهلن مدل Aura
    )
}