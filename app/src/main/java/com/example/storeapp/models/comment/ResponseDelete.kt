package com.example.storeapp.models.comment


import com.google.gson.annotations.SerializedName

data class ResponseDelete(
    @SerializedName("message")
    val message: String? // error.
)