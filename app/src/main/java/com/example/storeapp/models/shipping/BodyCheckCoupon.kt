package com.example.storeapp.models.shipping


import com.google.gson.annotations.SerializedName

data class BodyCheckCoupon(
    @SerializedName("couponId")
    var couponId: String?=null // poff-20
)