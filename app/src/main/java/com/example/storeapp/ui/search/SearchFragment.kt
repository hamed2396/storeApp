package com.example.storeapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.insets.showKeyboard
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.addDebounceTextListener
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.search.SearchAdapter
import com.example.storeapp.databinding.FragmentSearchBinding
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel by activityViewModels<SearchViewModel>()

    @Inject
    lateinit var searchAdapter: SearchAdapter
    private var searchText = ""
    private var sort = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            root.showKeyboard()

            filterImg.setOnClickListener {
                findNavController().navigate(R.id.actionSearchToFilter)
            }
            //toolbar
            toolbar.apply {
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarTitleTxt.text = getString(R.string.searchInProducts)
                toolbarOptionImg.gone()

            }

            searchEdt.addDebounceTextListener {
                searchText = it.toString()
                sort = sort.ifEmpty { Constants.SORT_NEW }
                val queries = viewModel.searchQueries(it.toString(), sort)
                viewModel.callSearchApi(queries)
                if (it.isEmpty()) {
                    searchList.gone()
                }
            }
            loadSearchData()
            loadFilterData()

        }
    }

    private fun loadFilterData() {
        viewModel.filterSelectedItem.observe(viewLifecycleOwner) {
            sort = it
            val queries = viewModel.searchQueries(searchText, it)
            queries.logError()
            isNetworkAvailable.ifTrue {
                viewModel.callSearchApi(queries)

            }
        }
    }

    private fun loadSearchData() {
        viewModel.searchData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        searchList.hideShimmer()
                        it.data?.products?.let { products ->
                            if (products.data?.isNotEmpty()!!) {
                                searchAdapter.setData(products.data)
                                searchList.initRecyclerViewAdapter(searchAdapter)
                                emptyLay.gone()
                                searchList.visible()
                            } else {
                                emptyLay.visible()
                                searchList.gone()
                            }
                        }
                    }

                    is NetworkStatus.Error -> {
                        searchList.hideShimmer()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        searchList.showShimmer()

                    }

                }
            }
        }

    }


}