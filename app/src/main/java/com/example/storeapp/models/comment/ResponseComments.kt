package com.example.storeapp.models.comment


import com.google.gson.annotations.SerializedName

data class ResponseComments(
    @SerializedName("current_page")
    val currentPage: Int?, // 1
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("first_page_url")
    val firstPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/comments?page=1
    @SerializedName("from")
    val from: Int?, // 1
    @SerializedName("last_page")
    val lastPage: Int?, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/comments?page=1
    @SerializedName("links")
    val links: List<Link?>?,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?, // null
    @SerializedName("path")
    val path: String?, // https://shop.nouri-api.ir/api/v1/auth/comments
    @SerializedName("per_page")
    val perPage: Int?, // 12
    @SerializedName("prev_page_url")
    val prevPageUrl: Any?, // null
    @SerializedName("to")
    val to: Int?, // 1
    @SerializedName("total")
    val total: Int? // 1
) {
    data class Data(
        @SerializedName("approved")
        val approved: String?, // 0
        @SerializedName("comment")
        val comment: String?, // Heh
        @SerializedName("created_at")
        val createdAt: String?, // 2023-10-19T16:37:09.000000Z
        @SerializedName("id")
        val id: Int?, // 96
        @SerializedName("product")
        val product: Product?,
        @SerializedName("product_id")
        val productId: String?, // 21
        @SerializedName("rate")
        val rate: String?, // 3
        @SerializedName("updated_at")
        val updatedAt: String?, // 2023-10-19T16:37:09.000000Z
        @SerializedName("user_id")
        val userId: String? // 21
    ) {
        data class Product(
            @SerializedName("id")
            val id: Int?, // 21
            @SerializedName("image")
            val image: String?, // /storage/cache/600-0-0-width-CPBDDMU5RAFd6IzzDJEe19RMCpDCkh5qKZapQqmP.jpg
            @SerializedName("title")
            val title: String? // جاروبرقی ویومی مدل S9
        )
    }

    data class Link(
        @SerializedName("active")
        val active: Boolean?, // false
        @SerializedName("label")
        val label: String?, // &laquo; قبلی
        @SerializedName("url")
        val url: String? // https://shop.nouri-api.ir/api/v1/auth/comments?page=1
    )
}