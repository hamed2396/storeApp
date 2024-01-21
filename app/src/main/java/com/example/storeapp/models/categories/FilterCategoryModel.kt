package com.example.storeapp.models.categories

data class FilterCategoryModel(
    val sort: String? = null,
    val minPrice: String? = null,
    val maxPrice: String? = null,
    val onlyAvailable: Boolean? = null,
    val search: String? = null
)
