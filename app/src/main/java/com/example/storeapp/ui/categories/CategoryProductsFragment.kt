package com.example.storeapp.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.categories.CategoryProductAdapter
import com.example.storeapp.databinding.FragmentCategoriesProductsBinding
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.CategoryProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryProductsFragment : BaseFragment(R.layout.fragment_categories_products) {

    private val binding by viewBinding(FragmentCategoriesProductsBinding::bind)
    private val args by navArgs<CategoryProductsFragmentArgs>()
    private var slug = ""
    private val viewModel by activityViewModels<CategoryProductViewModel>()

    @Inject
    lateinit var categoryProductAdapter: CategoryProductAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.let {
                slug = it.slug
            }
            toolbar.apply {
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarOptionImg.setOnClickListener { findNavController().navigate(R.id.categoryFilterFragment) }
            }
            isNetworkAvailable.ifTrue {
                viewModel.callCategoryProduct(slug, viewModel.productQueries())
            }
            loadProductsData()
            receiveCategoryFilter()
        }

    }

    private fun loadProductsData() {

        viewModel.categoryProductData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        binding.toolbar.toolbarTitleTxt.text = it.data?.subCategory?.title
                        productsList.hideShimmer()
                        it.data?.subCategory?.products?.let { products ->
                            if (products.data?.isNotEmpty()!!) {
                                categoryProductAdapter.setData(products.data)
                                productsList.initRecyclerViewAdapter(categoryProductAdapter)
                                emptyLay.gone()
                                productsList.visible()
                            } else {
                                emptyLay.visible()
                                productsList.gone()
                            }
                        }
                    }

                    is NetworkStatus.Error -> {
                        productsList.hideShimmer()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        productsList.showShimmer()

                    }

                }
            }
        }

    }

    private fun receiveCategoryFilter() {
        viewModel.getFilterCategory.observe(viewLifecycleOwner) {
            isNetworkAvailable.ifTrue {
                viewModel.callCategoryProduct(
                    slug,
                    viewModel.productQueries(
                        it.sort,
                        it.minPrice,
                        it.maxPrice,
                        null,
                        it.onlyAvailable,
                        it.search
                    )
                )
            }
        }
    }


}