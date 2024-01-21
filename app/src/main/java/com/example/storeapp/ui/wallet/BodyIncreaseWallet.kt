package com.example.storeapp.ui.wallet


import com.google.gson.annotations.SerializedName

data class BodyIncreaseWallet(
    @SerializedName("amount")
    var amount: String? = null // 190000
)