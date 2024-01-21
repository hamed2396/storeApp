package com.example.storeapp.models.details


import com.google.gson.annotations.SerializedName

class ResponseFeatures : ArrayList<ResponseFeatures.ResponseFeturesItem>(){
    data class ResponseFeturesItem(
        @SerializedName("featureItem_title")
        val featureItemTitle: String?, // دارد
        @SerializedName("feature_title")
        val featureTitle: String? // فلاش
    )
}