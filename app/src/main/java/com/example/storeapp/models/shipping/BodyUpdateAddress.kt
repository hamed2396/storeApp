package com.example.storeapp.models.shipping


import com.google.gson.annotations.SerializedName

data class BodyUpdateAddress(
    @SerializedName("addressId")
    var addressId: String? =null// 85
)