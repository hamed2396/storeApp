package com.example.storeapp.data.repository.search

import android.content.Context
import com.example.storeapp.R.string.filterCheep
import com.example.storeapp.R.string.filterExpensive
import com.example.storeapp.R.string.filterHits
import com.example.storeapp.R.string.filterNew
import com.example.storeapp.R.string.filterRate
import com.example.storeapp.R.string.filterSell
import com.example.storeapp.models.search.SearchFilterModel
import com.example.storeapp.utils.Constants.CHEEP
import com.example.storeapp.utils.Constants.EXPENSIVE
import com.example.storeapp.utils.Constants.HITS
import com.example.storeapp.utils.Constants.RATE
import com.example.storeapp.utils.Constants.SELL
import com.example.storeapp.utils.Constants.SORT_NEW
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchFilterRepository @Inject constructor(@ApplicationContext private val context: Context) {

    fun getFilterOptions(): MutableList<SearchFilterModel> {
        val filterOptions = mutableListOf<SearchFilterModel>()
        filterOptions.add(SearchFilterModel(1, context.getString(filterNew), SORT_NEW))
        filterOptions.add(SearchFilterModel(2, context.getString(filterExpensive), EXPENSIVE))
        filterOptions.add(SearchFilterModel(3, context.getString(filterCheep), CHEEP))
        filterOptions.add(SearchFilterModel(4, context.getString(filterRate), RATE))
        filterOptions.add(SearchFilterModel(5, context.getString(filterSell), SELL))
        filterOptions.add(SearchFilterModel(6, context.getString(filterHits), HITS))
        return filterOptions
    }
}

