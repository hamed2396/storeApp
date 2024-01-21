package com.example.storeapp.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.categories.CategoriesAdapter
import com.example.storeapp.databinding.FragmentCategoriesBinding
import com.example.storeapp.models.categories.ResponseCategories
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.CategoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : BaseFragment(R.layout.fragment_categories) {

    private val binding by viewBinding(FragmentCategoriesBinding::bind)
    private val viewModel by viewModels<CategoriesViewModel>()
    @Inject
    lateinit var categoriesAdapter: CategoriesAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {

                toolbarTitleTxt.text = getString(R.string.searchInProducts)
                toolbarOptionImg.gone()
                toolbarBackImg.gone()
                loadCategoryData()


        }

    }

    private fun loadCategoryData() {
        viewModel.categoriesData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {

                        categoriesList.hideShimmer()
                        it.data?.let {
                            setUpCategoryRecycler(it)

                        }

                    }

                    is NetworkStatus.Error -> {
                        categoriesList.hideShimmer()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        categoriesList.showShimmer()

                    }

                }
            }
        }

    }

    private fun setUpCategoryRecycler(data: List<ResponseCategories.ResponseCategoriesItem>) {
        binding.apply {
            categoriesAdapter.setData(data)
            categoriesList.initRecyclerViewAdapter(categoriesAdapter)
            categoriesAdapter.setOnItemClickListener {
                val direction =CategoryFragmentDirections.actionNavCatToNavCatProduct(it)
                findNavController().navigate(direction)
            }

        }
    }


}