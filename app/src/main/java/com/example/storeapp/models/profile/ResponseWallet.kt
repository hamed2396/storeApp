package com.example.storeapp.models.profile


import com.google.gson.annotations.SerializedName

data class ResponseWallet(
    @SerializedName("wallet")
    val wallet: String? // 6265542
)