package com.example.storeapp.models.cart


import com.google.gson.annotations.SerializedName

data class ResponseCart(
    @SerializedName("delivery_price")
    val deliveryPrice: String?, // 0
    @SerializedName("discounted_rate")
    val discountedRate: Int?, // 5
    @SerializedName("final_price")
    val finalPrice: String?, // 2375000
    @SerializedName("id")
    val id: Int?, // 239
    @SerializedName("items_discount")
    val itemsDiscount: Int?, // 125000
    @SerializedName("items_price")
    val itemsPrice: String?, // 2375000
    @SerializedName("order_items")
    val orderItems: List<OrderItem>?,
    @SerializedName("quantity")
    val quantity: String?, // 1
    @SerializedName("status")
    val status: String?, // add-to-cart
    @SerializedName("user_id")
    val userId: String? // 21
) {
    data class OrderItem(
        @SerializedName("approved")
        val approved: String?, // 1
        @SerializedName("color_hex_code")
        val colorHexCode: String?, // #000000
        @SerializedName("color_title")
        val colorTitle: String?, // مشکی
        @SerializedName("discounted_price")
        val discountedPrice: String?, // 125000
        @SerializedName("final_price")
        val finalPrice: String?, // 2375000
        @SerializedName("id")
        val id: Int?, // 544
        @SerializedName("order_id")
        val orderId: String?, // 239
        @SerializedName("price")
        val price: String?, // 2500000
        @SerializedName("product_guarantee")
        val productGuarantee: String?, // null
        @SerializedName("product_id")
        val productId: String?, // 53
        @SerializedName("product_image")
        val productImage: String?, // /storage/cache/600-0-0-width-BLm3XdhrYfUKQNoBXgFU9xFejqWme0dyMxPRBs6P.jpg
        @SerializedName("product_quantity")
        val productQuantity: String?, // 1
        @SerializedName("product_title")
        val productTitle: String?, // شارژر بی سیم فون مهلن مدل Aura
        @SerializedName("quantity")
        val quantity: String? // 1
    )
}