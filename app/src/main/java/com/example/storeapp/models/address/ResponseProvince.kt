package com.example.storeapp.models.address


import com.google.gson.annotations.SerializedName

class ResponseProvince : ArrayList<ResponseProvince.ResponseProvinceItem>(){
    data class ResponseProvinceItem(
        @SerializedName("id")
        val id: Int?, // 1
        @SerializedName("title")
        val title: String? // آذربایجان شرقی
    )
}