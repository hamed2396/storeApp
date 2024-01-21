package com.example.storeapp.ui.comments

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
import com.example.storeapp.adapters.comments.CommentsAdapter
import com.example.storeapp.databinding.FragmentProfileCommentsBinding
import com.example.storeapp.models.comment.ResponseComments
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.CommentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileCommentsFragment : BaseFragment(R.layout.fragment_profile_comments) {

    private val binding by viewBinding(FragmentProfileCommentsBinding::bind)
    private val viewModel by viewModels<CommentsViewModel>()

    @Inject
    lateinit var commentsAdapter: CommentsAdapter
    private var recyclerViewState: Parcelable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loadCommentsData()
            loadDeleteCommentsData()
            binding.toolbar.apply {
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarOptionImg.gone()
                toolbarTitleTxt.text = getString(R.string.yourComment)
            }
            isNetworkAvailable.ifTrue { viewModel.callGetCommentsApi() }

        }

    }

    private fun loadCommentsData() {
        viewModel.commentsData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        commentsList.hideShimmer()
                        it.data?.let { response ->
                            if (response.data.isNullOrEmpty().not()) {
                                setUpCommentList(response.data!!)
                                commentsList.visible()
                                emptyLay.gone()

                            } else {
                                emptyLay.visible()
                                commentsList.gone()
                            }
                        }
                    }

                    is NetworkStatus.Error -> {
                        commentsList.hideShimmer()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {

                        commentsList.showShimmer()

                    }

                }
            }
        }


    }

    private fun loadDeleteCommentsData() {
        viewModel.deleteCommentsData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        commentsList.hideShimmer()
                        it.data?.let { _ ->
                            isNetworkAvailable.ifTrue { viewModel.callGetCommentsApi() }

                        }
                    }

                    is NetworkStatus.Error -> { root.snackbar(it.error!!) }

                    is NetworkStatus.Loading -> {}

                }
            }
        }


    }

    private fun setUpCommentList(list: List<ResponseComments.Data>) {
        binding.apply {
            commentsAdapter.setData(list)
            commentsList.initRecyclerViewAdapter(commentsAdapter,)
            binding.commentsList.layoutManager?.onRestoreInstanceState(
                recyclerViewState
            )
            commentsAdapter.setOnItemClickListener {
                recyclerViewState = binding.commentsList.layoutManager?.onSaveInstanceState()
                isNetworkAvailable.ifTrue { viewModel.callDeleteCommentsApi(it) }
            }
        }
    }

}