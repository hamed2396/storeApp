package com.example.storeapp.models.login


import com.google.gson.annotations.SerializedName

data class ResponseVerify(
    @SerializedName("access_token")
    val accessToken: String?, // eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI0IiwianRpIjoiNTFmZjkwMTFmZTI1Y2RjYmMxYzYzN2Y4NTQ1MjRiNzk2ZDAxNzJkMGQ3MTJhODQ4MDA1ZjcwYjYwYzZjZTQwOGI4YzhiM2IxZTA4Nzc0NzEiLCJpYXQiOjE3MDE2OTQ5MjcuODMxMDMxLCJuYmYiOjE3MDE2OTQ5MjcuODMxMDMzLCJleHAiOjE3MTQ4Mjc3MjcuNzY3MDAyLCJzdWIiOiIyMSIsInNjb3BlcyI6W119.uN5dmuSbv09JGmwvoEPcSMROgy5wSoboQqlAirt9j1e6P19Iq-dH6eQHBJ3hMXwUa-WYyCPHuCDsIExv3ZRuzIbOdZLIERxJDTsRdAJ0C2XkGc4xHxrmRb7EYeCo0T7bmGdT5fh0rKMaK-QVBDKj5ii1az_8enGBIQRlmf4s0HQteyAWcqYbuZul0HAluK5YmneyD7_xhu5YE_w6adVgKg4D6i8I9aXcE9ZGAkRkPo6tG-uBn1I4of_i3nhpBdYdASRZSZodDfxkuvcwaRC1uNuzYgxVcOVCmuT0lAS7Pe7lJo-FiO0fuy7qVp02TETGqjXsdf9kucJYxDp3Rb6RLGI0i5P76Cu1UHUVBd22k8pppxoFlnCruXHo6nyrkaPIstvVUw93-EiHhHY9K60KxWVv506RePSLhVVMxXaGZBhKvLunQ9KDsOxQrNi0wvQONu9wKZiY8CatSSFlLXD-J_D2OTo947iimp90OXyu4Tq5do-BuQNJwPenFDGqmdICn_ncmCRtRUgLkLAzDVOx-kCG-oNfbKTeIbIa4TFhlvJ8yrPvJtvQ1C0QkQ3trWAfc7sHlvF5p5mEBnXnms0FGWidFt-aaT2fIAg4m3E7GCxL0sMvquSElesEtjy1xQ4EW1Zy9SbPwUCU-egXbcmnxmdRAfYZcAytX3DrHtXUNFo
    @SerializedName("expires_in")
    val expiresIn: Int?, // 13132800
    @SerializedName("refresh_token")
    val refreshToken: String?, // def502006ca15727d2e04b3186329b9b7469b199a2cdfd443abad03f171618c354022e547de1c3e2eef3a7e31356b491c317cd024c2bb84f0d92aaca68673cee7a9cbc4e43f68253b0c1b41797bd29d898e0a8ec78af566bf90630742eb83bcb80a419b1006e335a89298bf8df89b42719bb9e851e4a10bcb276fe080a1dd532b29bffe4b409173d4b9459dca473e88760636b77f5db030ebf72395cbfadc9ef7c4f27977128d98144fb2d9cc388189a1bb80b48b3e19745a996ba47c63afca6e5e15b03855bbaff39f209b1bfb68213773a0b440c3cd3eee9a8aca64c304557add8093aa9bb31a54773d51d9afa10177729f116c589adc96d39d9a0c9d565c20b9958a5f5b35b4310c472d455ac7c665d14f1982f3b2ccdea400a1d03c87ea433e2676b6a9a4766560e2fa16e3490e92c5a2b39f259efd29b93dfe54ca1ce84cd1f4a4b061c7b7bbba24a19ce635e0856e2e691e12da52dc3ad6e54b5473f6efd6b
    @SerializedName("token_type")
    val tokenType: String?, // Bearer
    @SerializedName("user_detail")
    val userDetail: UserDetail?
) {
    data class UserDetail(
        @SerializedName("avatar")
        val avatar: String?, // avatar/21.jpg?169773367928
        @SerializedName("bank_account")
        val bankAccount: Any?, // null
        @SerializedName("birth_date")
        val birthDate: String?, // 1991-06-02T19:30:00.000000Z
        @SerializedName("cellphone")
        val cellphone: String?, // 09354217374
        @SerializedName("email")
        val email: Any?, // null
        @SerializedName("email_verified_at")
        val emailVerifiedAt: Any?, // null
        @SerializedName("firstname")
        val firstname: String?, // Hamed
        @SerializedName("id")
        val id: Int?, // 21
        @SerializedName("id_number")
        val idNumber: Any?, // null
        @SerializedName("job_title")
        val jobTitle: Any?, // null
        @SerializedName("lastname")
        val lastname: String?, // Ak
        @SerializedName("type")
        val type: String?, // customer
        @SerializedName("updated")
        val updated: String?, // now
        @SerializedName("wallet")
        val wallet: String? // 6265542
    )
}