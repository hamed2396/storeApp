package com.example.storeapp.models.profile


import com.google.gson.annotations.SerializedName

data class ResponseOrders(
    @SerializedName("current_page")
    val currentPage: Int?, // 1
    @SerializedName("data")
    val `data`: List<Data>?,
    @SerializedName("first_page_url")
    val firstPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/orders?page=1
    @SerializedName("from")
    val from: Int?, // 1
    @SerializedName("last_page")
    val lastPage: Int?, // 1
    @SerializedName("last_page_url")
    val lastPageUrl: String?, // https://shop.nouri-api.ir/api/v1/auth/orders?page=1
    @SerializedName("links")
    val links: List<Link?>?,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?, // null
    @SerializedName("path")
    val path: String?, // https://shop.nouri-api.ir/api/v1/auth/orders
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
        @SerializedName("final_price")
        val finalPrice: String?, // 120728000
        @SerializedName("id")
        val id: Int?, // 37
        @SerializedName("order_items")
        val orderItems: List<OrderItem>?,
        @SerializedName("status")
        val status: String?, // payment-pending
        @SerializedName("updated_at")
        val updatedAt: String? // 2024-01-08T12:09:44.000000Z
    ) {
        data class OrderItem(
            @SerializedName("extras")
            val extras: Extras?,
            @SerializedName("order_id")
            val orderId: String?, // 37
            @SerializedName("product_id")
            val productId: String?, // 57
            @SerializedName("product_title")
            val productTitle: String? // دفتر مشق 100 برگ تیما مدل پیکاسو
        ) {
            data class Extras(
                @SerializedName("color")
                val color: String?, // 9
                @SerializedName("discounted_price")
                val discountedPrice: String?, // 0
                @SerializedName("guarantee")
                val guarantee: String?, // گارانتی اصالت و سلامت فیزیکی کالا
                @SerializedName("id")
                val id: Int?, // 57
                @SerializedName("image")
                val image: String?, // /storage/cache/150-0-0-width-fipyiGff2vvYnS4loHA6HyfrK508mCr5v0Mwbr6r.jpg
                @SerializedName("price")
                val price: String?, // 22000
                @SerializedName("quantity")
                val quantity: String?, // 3
                @SerializedName("title")
                val title: String? // دفتر مشق 100 برگ تیما مدل پیکاسو
            )
        }
    }

    data class Link(
        @SerializedName("active")
        val active: Boolean?, // false
        @SerializedName("label")
        val label: String?, // &laquo; قبلی
        @SerializedName("url")
        val url: String? // https://shop.nouri-api.ir/api/v1/auth/orders?page=1
    )
}