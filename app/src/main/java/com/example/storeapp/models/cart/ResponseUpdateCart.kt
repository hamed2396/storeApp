package com.example.storeapp.models.cart


import com.google.gson.annotations.SerializedName

data class ResponseUpdateCart(
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
    @SerializedName("product")
    val product: Product?,
    @SerializedName("product_id")
    val productId: String?, // 53
    @SerializedName("quantity")
    val quantity: String?, // 1
    @SerializedName("user_id")
    val userId: String? // 21
) {
    data class Product(
        @SerializedName("approved")
        val approved: String?, // 1
        @SerializedName("brand_id")
        val brandId: Any?, // null
        @SerializedName("buy_price")
        val buyPrice: String?, // 1750000
        @SerializedName("category_id")
        val categoryId: String?, // 27
        @SerializedName("color_id")
        val colorId: List<String?>?,
        @SerializedName("created_at")
        val createdAt: String?, // 2022-01-01T13:38:36.000000Z
        @SerializedName("description")
        val description: String?, // <p>طراحی مینیمال و بسیار جذاب. متریال با کیفیت و شارژ فوق العاده سریع. اینها همه تعارفی از وایرلس شارژر فون مهلن مدل آورا ( Aura ) از شمال آلمان است. این شارژر وایرلس از سرتیفیکیت Qi نیز بهره مند است تا با تمامی دستگاه ها سازگاری کامل را داشته باشد. Aura قسمتی از خانه شما، هر جا که شما باشید. از دیگر ویژگی های آن میتوان به: &nbsp;قابلیت شارژ تلفن همراه هوشمند با قابی به ضخامت ۱ سانتی متر، رینگ سیلیکونی زیر شارژر برای جلوگیری از سرخوردن آن روی سطوح ، قابلیت هوشمند کنترل میزان شارژ و جلوگیری از شارژ اضافی ، چرم طبیعی گیاهی، &nbsp;مجهز به سیستم شناسایی جسم خارجی همچون کلید و شارژ سریع اشاره کرد.</p>
        @SerializedName("dimension")
        val dimension: Any?, // null
        @SerializedName("discount_rate")
        val discountRate: String?, // 5
        @SerializedName("discounted_price")
        val discountedPrice: String?, // 125000
        @SerializedName("end_time")
        val endTime: String?, // 2024-03-19T09:37:00.000000Z
        @SerializedName("final_price")
        val finalPrice: Int?, // 2375000
        @SerializedName("guarantee")
        val guarantee: Any?, // null
        @SerializedName("hits")
        val hits: String?, // 452
        @SerializedName("id")
        val id: Int?, // 53
        @SerializedName("image")
        val image: String?, // media/images/products/BLm3XdhrYfUKQNoBXgFU9xFejqWme0dyMxPRBs6P.jpg
        @SerializedName("images")
        val images: String?, // [{"image":"e6e7f365396ed443208cf952161fdf5c15b47b80_1622221082.jpg","url":"/storage/media/images/products/dEh8kMwLFi1ikxhDiDSWfLK4l2XFaOTut3MDAKXU.jpg"}]
        @SerializedName("ordering")
        val ordering: Any?, // null
        @SerializedName("product_price")
        val productPrice: String?, // 2500000
        @SerializedName("quantity")
        val quantity: String?, // 1
        @SerializedName("seo_description")
        val seoDescription: Any?, // null
        @SerializedName("seo_keywords")
        val seoKeywords: Any?, // null
        @SerializedName("seo_title")
        val seoTitle: Any?, // null
        @SerializedName("slug")
        val slug: Any?, // null
        @SerializedName("status")
        val status: String?, // discount
        @SerializedName("title")
        val title: String?, // شارژر بی سیم فون مهلن مدل Aura
        @SerializedName("title_en")
        val titleEn: Any?, // null
        @SerializedName("unit")
        val unit: String?, // عدد
        @SerializedName("updated_at")
        val updatedAt: String?, // 2024-01-16T09:32:05.000000Z
        @SerializedName("weight")
        val weight: String?, // 100.000
        @SerializedName("weight_unit")
        val weightUnit: String? // گرم
    )
}