package com.example.storeapp.models.details


import com.google.gson.annotations.SerializedName

data class BodyAddComment(
    @SerializedName("comment")
    var comment: String?=null,
    @SerializedName("rate")
    var rate: String?=null
)