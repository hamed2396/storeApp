package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.categories.CategoriesProductRepository
import com.example.storeapp.data.repository.search.SearchFilterRepository
import com.example.storeapp.models.categories.FilterCategoryModel
import com.example.storeapp.models.home.ResponseProducts
import com.example.storeapp.models.search.SearchFilterModel
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductViewModel @Inject constructor(
    private val repository: CategoriesProductRepository,
    private val networkResponse: NetworkResponse,
    private val filterRepository: SearchFilterRepository
) : ViewModel() {

    //products

    fun productQueries(
        sort: String? = null,
        minPrice: String? = null,
        maxPrice: String? = null,
        selectedBrands: String? = null,
        onlyAvailable: Boolean? = null,
        search: String? = null
    ): HashMap<String, String> {
        val queries = hashMapOf<String, String>()
        sort?.let { queries[Constants.SORT] = it }
        minPrice?.let { queries[Constants.MIN_PRICE] = it }
        maxPrice?.let { queries[Constants.MAX_PRICE] = it }
        selectedBrands?.let { queries[Constants.SELECTED_BRANDS] = it }
        onlyAvailable?.let { queries[Constants.ONLY_AVAILABLE] = it.toString() }
        search?.let { queries[Constants.SEARCH] = it }
        return queries
    }

    private val _productData = MutableLiveData<NetworkStatus<ResponseProducts>>()
    val categoryProductData get() = _productData as LiveData<NetworkStatus<ResponseProducts>>
    fun callCategoryProduct(slug: String, queries: Map<String, String>) =
        viewModelScope.launch {

            _productData.postValue(NetworkStatus.Loading())
            val response = repository.getProductCategories(slug, queries)
            _productData.postValue(networkResponse.handleResponse(response))
        }

    private val _filter = MutableLiveData<MutableList<SearchFilterModel>>()
    val filterData get() = _filter as LiveData<MutableList<SearchFilterModel>>
    fun getFilterList() = viewModelScope.launch {
        _filter.postValue(filterRepository.getFilterOptions())

    }

    private val _filterCategory = MutableLiveData<FilterCategoryModel>()
    val getFilterCategory get() = _filterCategory as LiveData<FilterCategoryModel>
    fun sendFilterCategoryItem(
        sort: String? = null,
        minPrice: String? = null,
        maxPrice: String? = null,
        onlyAvailable: Boolean? = null,
        search: String? = null
    ) {
        _filterCategory.postValue(
            FilterCategoryModel(
                sort,
                minPrice,
                maxPrice,
                onlyAvailable,
                search
            )
        )
    }

}