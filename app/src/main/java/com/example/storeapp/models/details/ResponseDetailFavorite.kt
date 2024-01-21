package com.example.storeapp.models.details


import com.google.gson.annotations.SerializedName

data class ResponseDetailFavorite(
    @SerializedName("count")
    val count: Int?, // 1
    @SerializedName("message")
    val message: String? // add_favorite
)