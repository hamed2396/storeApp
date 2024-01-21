package com.example.storeapp.models.shipping


import com.google.gson.annotations.SerializedName

data class ResponseShipment(
    @SerializedName("addresses")
    val addresses: List<Addresse>?,
    @SerializedName("order")
    val order: Order?,
    @SerializedName("status")
    val status: Boolean? // true
) {
    data class Addresse(
        @SerializedName("id")
        val id: Int?, // 80
        @SerializedName("postal_address")
        val postalAddress: String?, // نات
        @SerializedName("receiver_cellphone")
        val receiverCellphone: String?, // 09354217374
        @SerializedName("receiver_firstname")
        val receiverFirstname: String?, // حامد
        @SerializedName("receiver_lastname")
        val receiverLastname: String? // ا
    )

    data class Order(
        @SerializedName("address")
        val address: Address?,
        @SerializedName("address_id")
        val addressId: String?, // 80
        @SerializedName("delivery_price")
        val deliveryPrice: String?, // 0
        @SerializedName("discounted_rate")
        val discountedRate: Int?, // 5
        @SerializedName("final_price")
        val finalPrice: String?, // 2375000
        @SerializedName("id")
        val id: Int?, // 240
        @SerializedName("items_discount")
        val itemsDiscount: String?, // 125000
        @SerializedName("items_price")
        val itemsPrice: String?, // 2375000
        @SerializedName("order_items")
        val orderItems: List<OrderItem>?,
        @SerializedName("quantity")
        val quantity: String? // 1
    ) {
        data class Address(
            @SerializedName("id")
            val id: Int?, // 80
            @SerializedName("postal_address")
            val postalAddress: String?, // نات
            @SerializedName("receiver_cellphone")
            val receiverCellphone: String?, // 09354217374
            @SerializedName("receiver_firstname")
            val receiverFirstname: String?, // حامد
            @SerializedName("receiver_lastname")
            val receiverLastname: String? // ا
        )

        data class OrderItem(
            @SerializedName("color_hex_code")
            val colorHexCode: String?, // #000000
            @SerializedName("color_title")
            val colorTitle: String?, // مشکی
            @SerializedName("discounted_price")
            val discountedPrice: String?, // 125000
            @SerializedName("order_id")
            val orderId: String?, // 240
            @SerializedName("product_id")
            val productId: String?, // 53
            @SerializedName("product_image")
            val productImage: String?, // /storage/cache/300-0-0-width-BLm3XdhrYfUKQNoBXgFU9xFejqWme0dyMxPRBs6P.jpg
            @SerializedName("product_title")
            val productTitle: String?, // شارژر بی سیم فون مهلن مدل Aura
            @SerializedName("quantity")
            val quantity: String? // 1
        )
    }
}