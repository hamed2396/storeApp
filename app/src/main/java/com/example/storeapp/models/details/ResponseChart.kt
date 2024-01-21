package com.example.storeapp.models.details


import com.google.gson.annotations.SerializedName

class ResponseChart : ArrayList<ResponseChart.ResponseChartItem>(){
    data class ResponseChartItem(
        @SerializedName("day")
        val day: String?, // 1402-10-25
        @SerializedName("price")
        val price: Int? // 13704550
    )
}