package com.example.storeapp.utils

import com.example.storeapp.utils.Constants.CATEGORY_LAPTOP
import com.example.storeapp.utils.Constants.CATEGORY_MEN_SHOES
import com.example.storeapp.utils.Constants.CATEGORY_MOBILE
import com.example.storeapp.utils.Constants.CATEGORY_STATIONERY

enum class ProductsCategories(val category: String) {

    MOBILE(CATEGORY_MOBILE),
    SHOES(CATEGORY_MEN_SHOES),
    STATIONARY(CATEGORY_STATIONERY),
    LAPTOP(CATEGORY_LAPTOP)

}