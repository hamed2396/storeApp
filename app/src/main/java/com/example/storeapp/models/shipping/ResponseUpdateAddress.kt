package com.example.storeapp.models.shipping


import com.google.gson.annotations.SerializedName

data class ResponseUpdateAddress(
    @SerializedName("address")
    val address: Address?,
    @SerializedName("message")
    val message: String? // آدرس دریافت بروزرسانی شد.
) {
    data class Address(
        @SerializedName("id")
        val id: Int?, // 85
        @SerializedName("postal_address")
        val postalAddress: String?, // Ff
        @SerializedName("receiver_cellphone")
        val receiverCellphone: String?, // 09999999999
        @SerializedName("receiver_firstname")
        val receiverFirstname: String?, // B
        @SerializedName("receiver_lastname")
        val receiverLastname: String? // B
    )
}