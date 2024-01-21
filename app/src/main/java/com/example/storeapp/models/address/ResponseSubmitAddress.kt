package com.example.storeapp.models.address


import com.google.gson.annotations.SerializedName

data class ResponseSubmitAddress(
    @SerializedName("city_id")
    val cityId: String?, // 240
    @SerializedName("created_at")
    val createdAt: String?, // 2024-01-04T14:08:22.000000Z
    @SerializedName("floor")
    val floor: String?, // 1
    @SerializedName("id")
    val id: Int?, // 81
    @SerializedName("latitude")
    val latitude: String?, // ع1.
    @SerializedName("longitude")
    val longitude: String?, // 20.2
    @SerializedName("plate_number")
    val plateNumber: String?, // 011
    @SerializedName("postal_address")
    val postalAddress: String?, // 1111111111
    @SerializedName("postal_code")
    val postalCode: String?, // 1111111111
    @SerializedName("province_id")
    val provinceId: String?, // 5
    @SerializedName("receiver_cellphone")
    val receiverCellphone: String?, // 10101010101
    @SerializedName("receiver_firstname")
    val receiverFirstname: String?, // نام گیرنده
    @SerializedName("receiver_lastname")
    val receiverLastname: String?, // نام خانوادگی گیرنده
    @SerializedName("updated_at")
    val updatedAt: String?, // 2024-01-04T14:08:22.000000Z
    @SerializedName("user_id")
    val userId: Int? // 21
)