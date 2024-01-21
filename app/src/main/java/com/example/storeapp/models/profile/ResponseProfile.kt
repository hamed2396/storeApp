package com.example.storeapp.models.profile


import com.google.gson.annotations.SerializedName
data class ResponseProfile(
    @SerializedName("avatar")
    val avatar: String?, // https://shop.nouri-api.ir/avatar/21.jpg?169773367928
    @SerializedName("birth_date")
    val birthDate: String?, // 1370-03-12T20:34:16.000000Z
    @SerializedName("cellphone")
    val cellphone: String?, // 09354217374
    @SerializedName("email")
    val email: Any?, // null
    @SerializedName("firstname")
    val firstname: String?, // Hamed
    @SerializedName("id")
    val id: Int?, // 21
    @SerializedName("idNumber")
    val idNumber: Any?, // null
    @SerializedName("job")
    val job: Any?, // null
    @SerializedName("lastname")
    val lastname: String?, // Akk
    @SerializedName("register_date")
    val registerDate: String?, // 13 ، خرد ، 02
    @SerializedName("wallet")
    val wallet: String? // 6265542
)