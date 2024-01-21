package com.example.storeapp.models.favorite


import com.google.gson.annotations.SerializedName

data class ResponseFavorite(
    @SerializedName("current_page")
    val currentPage: Int?, // 1
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("first_page_url")
    val firstPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/favorites?page=1
    @SerializedName("from")
    val from: Int?, // 1
    @SerializedName("last_page")
    val lastPage: Int?, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/favorites?page=1
    @SerializedName("links")
    val links: List<Link?>?,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?, // null
    @SerializedName("path")
    val path: String?, // https://shop.nouri-api.ir/api/v1/auth/favorites
    @SerializedName("per_page")
    val perPage: Int?, // 10
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?, // null
    @SerializedName("to")
    val to: Int?, // 1
    @SerializedName("total")
    val total: Int? // 1
) {
    data class Data(
        @SerializedName("created_at")
        val createdAt: String?, // 2023-07-15T17:23:23.000000Z
        @SerializedName("id")
        val id: Int?, // 137
        @SerializedName("likeable")
        val likeable: Likeable?,
        @SerializedName("likeable_id")
        val likeableId: String?, // 51
        @SerializedName("likeable_type")
        val likeableType: String?, // App\Models\Product
        @SerializedName("updated_at")
        val updatedAt: String?, // 2023-07-15T17:23:23.000000Z
        @SerializedName("user_id")
        val userId: String? // 21
    ) {
        data class Likeable(
            @SerializedName("discount_rate")
            val discountRate: String?, // 5
            @SerializedName("discounted_price")
            val discountedPrice: String?, // 2400
            @SerializedName("end_time")
            val endTime: String?, // 2024-03-19T09:37:00.000000Z
            @SerializedName("final_price")
            val finalPrice: Int?, // 45600
            @SerializedName("id")
            val id: Int?, // 51
            @SerializedName("image")
            val image: String?, // /storage/cache/600-0-0-width-nHiU2xIJaHj75vjnQiF3SCLvKplQcmlXBSfDs4Dn.jpg
            @SerializedName("product_price")
            val productPrice: String?, // 48000
            @SerializedName("quantity")
            val quantity: String?, // 0
            @SerializedName("status")
            val status: String?, // discount
            @SerializedName("title")
            val title: String? // کاور مدل SLCN مناسب برای گوشی موبایل سامسونگ Galaxy A51
        )
    }

    data class Link(
        @SerializedName("active")
        val active: Boolean?, // false
        @SerializedName("label")
        val label: String?, // &laquo; قبلی
        @SerializedName("url")
        val url: String? // https://shop.nouri-api.ir/api/v1/auth/favorites?page=1
    )
}