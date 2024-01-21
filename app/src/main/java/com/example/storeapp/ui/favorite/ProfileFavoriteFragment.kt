package com.example.storeapp.ui.favorite

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.favorite.FavoriteAdapter
import com.example.storeapp.databinding.FragmentProfileFavoriteBinding
import com.example.storeapp.models.favorite.ResponseFavorite
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFavoriteFragment : BaseFragment(R.layout.fragment_profile_favorite) {

    private val binding by viewBinding(FragmentProfileFavoriteBinding::bind)
    private val viewModel by viewModels<FavoriteViewModel>()

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter
    private var recyclerViewState: Parcelable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loadFavoritesData()
            loadDeleteFavoriteData()
            binding.toolbar.apply {
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarOptionImg.gone()
                toolbarTitleTxt.text = getString(R.string.yourFavorites)
            }


        }

    }
    private fun loadFavoritesData() {
        viewModel.favoriteData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        favoritesList.hideShimmer()
                        it.data?.let { response ->
                            if (response.data.isNullOrEmpty().not()) {
                                setUpCommentList(response.data!!)
                                favoritesList.visible()
                                emptyLay.gone()

                            } else {
                                emptyLay.visible()
                                favoritesList.gone()
                            }
                        }
                    }

                    is NetworkStatus.Error -> {
                        favoritesList.hideShimmer()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {

                        favoritesList.showShimmer()

                    }

                }
            }
        }


    }

    private fun loadDeleteFavoriteData() {
        viewModel.favoriteDeleteData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        favoritesList.hideShimmer()
                        it.data?.let { _ ->
                            isNetworkAvailable.ifTrue { viewModel.callFavoriteApi() }

                        }
                    }

                    is NetworkStatus.Error -> { root.snackbar(it.error!!) }

                    is NetworkStatus.Loading -> {}

                }
            }
        }


    }

    private fun setUpCommentList(list: List<ResponseFavorite.Data>) {
        binding.apply {
            favoriteAdapter.setData(list)
            favoritesList.initRecyclerViewAdapter(favoriteAdapter)
            binding.favoritesList.layoutManager?.onRestoreInstanceState(
                recyclerViewState
            )
            favoriteAdapter.setOnItemClickListener {
                recyclerViewState = binding.favoritesList.layoutManager?.onSaveInstanceState()
                isNetworkAvailable.ifTrue { viewModel.callDeleteFavoriteApi(it) }
            }
        }
    }


}