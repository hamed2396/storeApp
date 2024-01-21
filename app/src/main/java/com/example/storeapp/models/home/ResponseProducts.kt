package com.example.storeapp.models.home


import com.example.storeapp.models.search.ResponseSearch
import com.google.gson.annotations.SerializedName

data class ResponseProducts(
    @SerializedName("subCategory")
    val subCategory: SubCategory?
) {
    data class SubCategory(
        @SerializedName("id")
        val id: Int?, // 26
        @SerializedName("parent")
        val parent: Parent?,
        @SerializedName("parent_id")
        val parentId: String?, // 11
        @SerializedName("products")
        val products: Products?,
        @SerializedName("products_count")
        val productsCount: Int?, // 8
        @SerializedName("slug")
        val slug: String?, // category-mobile-phone
        @SerializedName("sub_categories")
        val subCategories: List<Any?>?,
        @SerializedName("title")
        val title: String? // گوشی موبایل
    ) {
        data class Parent(
            @SerializedName("id")
            val id: Int?, // 11
            @SerializedName("parent")
            val parent: Parent?,
            @SerializedName("parent_id")
            val parentId: String?, // 1
            @SerializedName("slug")
            val slug: String?, // category-mobile
            @SerializedName("title")
            val title: String? // موبایل
        ) {
            data class Parent(
                @SerializedName("id")
                val id: Int?, // 1
                @SerializedName("parent")
                val parent: Any?, // null
                @SerializedName("parent_id")
                val parentId: String?, // 0
                @SerializedName("slug")
                val slug: String?, // electronic-devices
                @SerializedName("title")
                val title: String? // کالای دیجیتال
            )
        }

        data class Products(
            @SerializedName("current_page")
            val currentPage: Int?, // 1
            @SerializedName("data")
            val `data`: List<Data>?,
            @SerializedName("first_page_url")
            val firstPageUrl: String?, // https://shop.nouri-api.ir/api/v1/category/pro/category-mobile-phone?page=1
            @SerializedName("from")
            val from: Int?, // 1
            @SerializedName("last_page")
            val lastPage: Int?, // 1
            @SerializedName("last_page_url")
            val lastPageUrl: String?, // https://shop.nouri-api.ir/api/v1/category/pro/category-mobile-phone?page=1
            @SerializedName("links")
            val links: List<Link?>?,
            @SerializedName("next_page_url")
            val nextPageUrl: Any?, // null
            @SerializedName("path")
            val path: String?, // https://shop.nouri-api.ir/api/v1/category/pro/category-mobile-phone
            @SerializedName("per_page")
            val perPage: Int?, // 16
            @SerializedName("prev_page_url")
            val prevPageUrl: Any?, // null
            @SerializedName("to")
            val to: Int?, // 8
            @SerializedName("total")
            val total: Int? // 8
        ) {
            data class Data(
                @SerializedName("color_id")
                val colorId: List<String?>?,
                @SerializedName("colors")
                val colors: List<ResponseSearch.Products.Data.Color>?,
                @SerializedName("comments_avg_rate")
                val commentsAvgRate: String?, // 4.0000
                @SerializedName("comments_count")
                val commentsCount: String?, // 0
                @SerializedName("created_at")
                val createdAt: String?, // 2021-12-30T13:55:06.000000Z
                @SerializedName("discount_rate")
                val discountRate: String?, // 0
                @SerializedName("discounted_price")
                val discountedPrice: Int?, // 0
                @SerializedName("end_time")
                val endTime: String?, // 2022-01-20T13:39:00.000000Z
                @SerializedName("final_price")
                val finalPrice: Int?, // 2607000
                @SerializedName("id")
                val id: Int?, // 33
                @SerializedName("image")
                val image: String?, // /storage/cache/400-0-0-width-cy0yLo6Nva1oqtaLoIpJ3FSBJ9uTVFMuYTmZiUBX.jpg
                @SerializedName("product_price")
                val productPrice: String?, // 13650000
                @SerializedName("quantity")
                val quantity: String?, // 0
                @SerializedName("status")
                val status: String?, // normal
                @SerializedName("title")
                val title: String?, // گوشی موبایل سامسونگ مدل Galaxy S20 FE 5G SM-G781BDS
                @SerializedName("title_en")
                val titleEn: Any? // null
            )

            data class Link(
                @SerializedName("active")
                val active: Boolean?, // false
                @SerializedName("label")
                val label: String?, // &laquo; قبلی
                @SerializedName("url")
                val url: String? // https://shop.nouri-api.ir/api/v1/category/pro/category-mobile-phone?page=1
            )
        }
    }
}