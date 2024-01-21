package com.example.storeapp.models.address


import com.google.gson.annotations.SerializedName

data class BodyAddresses(
    @SerializedName("addressId")
    var addressId: String? = null, // 110
    @SerializedName("longitude")
    var longitude: Float? = 1.5f,
    @SerializedName("latitude")
    var latitude: Float? = 2.5f,
    @SerializedName("cityId")
    var cityId: String? = null, // شناسه شهر
    @SerializedName("floor")
    var floor: String? = null, // طبقه
    @SerializedName("plateNumber")
    var plateNumber: String? = null, // شماره پلاک
    @SerializedName("postalAddress")
    var postalAddress: String? = null, // آدرس پستی
    @SerializedName("postalCode")
    var postalCode: String? = null, // کد پستی
    @SerializedName("provinceId")
    var provinceId: String? = null, // شناسه استان
    @SerializedName("receiverCellphone")
    var receiverCellphone: String? = null, // تلفن همراه گیرنده
    @SerializedName("receiverFirstname")
    var receiverFirstname: String? = null, // نام گیرنده
    @SerializedName("receiverLastname")
    var receiverLastname: String? = null // نام خانوادگی گیرنده
)