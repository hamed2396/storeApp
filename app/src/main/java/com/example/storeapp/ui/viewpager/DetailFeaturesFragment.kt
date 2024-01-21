package com.example.storeapp.ui.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.detail.FeaturesAdapter
import com.example.storeapp.databinding.FragmentDetailFeaturesBinding
import com.example.storeapp.models.details.ResponseFeatures
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFeaturesFragment : BaseFragment(R.layout.fragment_detail_features) {

    private val binding by viewBinding(FragmentDetailFeaturesBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()

    @Inject
    lateinit var featuresAdapter: FeaturesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.callGetFeaturesApi(Constants.PRODUCT_ID)
        loadFeaturesData()


    }

    private fun loadFeaturesData() {
        viewModel.featuresData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        featuresLoading.isVisible(false, featuresList)
                        if (it.data.isNotNullOrEmpty) {
                            emptyLay.gone()
                            setUpFeaturesList(it.data!!)

                        } else {
                            emptyLay.visible()
                        }

                    }

                    is NetworkStatus.Error -> {
                        featuresLoading.isVisible(false, featuresList)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        featuresLoading.isVisible(true, featuresList)


                    }

                }
            }
        }

    }

    private fun setUpFeaturesList(list: List<ResponseFeatures.ResponseFeturesItem>) {
        binding.apply {
            featuresAdapter.setData(list)
            featuresList.initRecyclerViewAdapter(
                featuresAdapter,
            )

        }
    }


}