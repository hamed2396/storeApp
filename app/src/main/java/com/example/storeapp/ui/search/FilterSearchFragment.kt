package com.example.storeapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.crazylegend.kotlinextensions.insets.hideKeyboard
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.search.FilterAdapter
import com.example.storeapp.databinding.FragmentSearchFilterBinding
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.viewModels.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class FilterSearchFragment : BottomSheetDialogFragment(R.layout.fragment_search_filter) {
    private val binding by viewBinding(FragmentSearchFilterBinding::bind)

    @Inject
    lateinit var filterAdapter: FilterAdapter
    private val viewModel by activityViewModels<SearchViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            closeImg.setOnClickListener { dismiss() }
            loadFilterData()

        }
    }

    private fun loadFilterData() {
        binding.apply {
            viewModel.getFilterList()
            viewModel.filterData.observe(viewLifecycleOwner) {
                filterAdapter.setData(it)
                filtersList.initRecyclerViewAdapter(filterAdapter, fixedSize = true)
                filterAdapter.setOnItemClickListener { item ->
                    viewModel.sendSelectedFilterItem(item)
                    dismiss()

                }
            }
        }

    }
    override fun getTheme() = R.style.RemoveDialogBackground


}