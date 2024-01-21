package com.example.storeapp.models.address


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

class ResponseAddresses : ArrayList<ResponseAddresses.ResponseAddressesItem>(){
    @Parcelize
    data class ResponseAddressesItem(
        @SerializedName("approved")
        val approved: String?, // 1
        @SerializedName("city")
        val city:@RawValue City?,
        @SerializedName("city_id")
        val cityId: String?, // 137
        @SerializedName("created_at")
        val createdAt: String?, // 2024-01-04T08:47:51.000000Z
        @SerializedName("deleted_at")
        val deletedAt:@RawValue Any?, // null
        @SerializedName("floor")
        val floor: String?, // 3
        @SerializedName("id")
        val id: Int?, // 80
        @SerializedName("latitude")
        val latitude: String?, // 4.00000000
        @SerializedName("longitude")
        val longitude: String?, // 5.00000000
        @SerializedName("plate_number")
        val plateNumber: String?, // 9
        @SerializedName("postal_address")
        val postalAddress: String?, // ذ
        @SerializedName("postal_code")
        val postalCode: String?, // 6829689996
        @SerializedName("province")
        val province: @RawValue Province?,
        @SerializedName("province_id")
        val provinceId: String?, // 4
        @SerializedName("receiver_cellphone")
        val receiverCellphone: String?, // 09354217374
        @SerializedName("receiver_firstname")
        val receiverFirstname: String?, // ح
        @SerializedName("receiver_lastname")
        val receiverLastname: String?, // ا
        @SerializedName("updated_at")
        val updatedAt: String?, // 2024-01-04T08:47:51.000000Z
        @SerializedName("user_id")
        val userId: String? // 21
    ):Parcelable {
        data class City(
            @SerializedName("id")
            val id: Int?, // 137
            @SerializedName("title")
            val title: String? // مهاباد
        )
    
        data class Province(
            @SerializedName("id")
            val id: Int?, // 4
            @SerializedName("title")
            val title: String? // اصفهان
        )
    }
}