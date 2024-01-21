package com.example.storeapp.ui.viewpager.comments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.crazylegend.kotlinextensions.collections.isListAndNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.detail.CommentsAdapter
import com.example.storeapp.databinding.FragmentDetailCommentsBinding
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailCommentFragment : BaseFragment(R.layout.fragment_detail_comments) {

    private val binding by viewBinding(FragmentDetailCommentsBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()

    @Inject
    lateinit var commentsAdapter: CommentsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.callGetCommentApi(Constants.PRODUCT_ID)
        loadProductCommentData()
        binding.addNewCommentTxt.setOnClickListener {
            findNavController().navigate(R.id.actionToAddComment)
        }


    }

    private fun loadProductCommentData() {
        viewModel.getCommentData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        commentsLoading.gone()
                        it.data?.data.isListAndNotNullOrEmpty({
                            emptyLay.visible()
                        }, {

                            emptyLay.gone()
                            commentsAdapter.setData(it.data!!.data!!)
                            commentsList.initRecyclerViewAdapter(
                                commentsAdapter,
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL, true
                                )
                            )

                        })


                    }

                    is NetworkStatus.Error -> {
                        commentsLoading.gone()

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        commentsLoading.visible()


                    }

                }
            }
        }

    }

}