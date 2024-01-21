package com.example.storeapp.models.login


import com.google.gson.annotations.SerializedName

data class BodyLogin(

    @SerializedName("login")
    var login: String? = null, // 0901111111
    @SerializedName("hash_code")
    var hashCode: String? = null, // dsjkawlsyk
    @SerializedName("code")
    var code: Int? = null, // 6532

)