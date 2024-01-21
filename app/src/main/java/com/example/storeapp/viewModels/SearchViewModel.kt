package com.example.storeapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storeapp.data.repository.search.SearchFilterRepository
import com.example.storeapp.data.repository.search.SearchRepository
import com.example.storeapp.models.search.ResponseSearch
import com.example.storeapp.models.search.SearchFilterModel
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.network.NetworkResponse
import com.example.storeapp.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val networkResponse: NetworkResponse,
    private val filterRepository: SearchFilterRepository
) : ViewModel() {
    //SEARCH
    fun searchQueries(search: String, sort: String): HashMap<String, String> {
        val queries = hashMapOf<String, String>()
        queries[Constants.SORT] = sort
        queries[Constants.Q] = search
        return queries
    }

    private val _search = MutableLiveData<NetworkStatus<ResponseSearch>>()
    val searchData get() = _search as LiveData<NetworkStatus<ResponseSearch>>
    fun callSearchApi(search: HashMap<String, String>) = viewModelScope.launch {
        _search.postValue(NetworkStatus.Loading())
        val response = repository.searchProduct(search)
        _search.postValue(networkResponse.handleResponse(response))
    }

    //filter
    private val _filter = MutableLiveData<MutableList<SearchFilterModel>>()
    val filterData get() = _filter as LiveData<MutableList<SearchFilterModel>>
    fun getFilterList() = viewModelScope.launch {
        _filter.postValue(filterRepository.getFilterOptions())

    }

    //filterSelected
    private val _filterSelectedItem = MutableLiveData<String>()
    val filterSelectedItem get() = _filterSelectedItem as LiveData<String>
    fun sendSelectedFilterItem(item: String) {
        _filterSelectedItem.postValue(item)
    }

}