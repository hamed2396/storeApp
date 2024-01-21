package com.example.storeapp.ui.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.orders.OrdersAdapter
import com.example.storeapp.databinding.FragmentProfileOrdersBinding
import com.example.storeapp.models.profile.ResponseOrders
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : BaseFragment(R.layout.fragment_profile_orders) {
    private val binding by viewBinding(FragmentProfileOrdersBinding::bind)
    private val viewModel by viewModels<OrdersViewModel>()
    private val args by navArgs<OrdersFragmentArgs>()
    private var status = ""
    @Inject lateinit var ordersAdapter: OrdersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.let { status = it.status }
            isNetworkAvailable.ifTrue { viewModel.callOrdersApi(status) }
            toolbar.apply {
                toolbarTitleTxt.text = when (status) {
                    Constants.CANCELED -> getString(R.string.canceled)
                    Constants.PENDING -> getString(R.string.pending)
                    Constants.DELIVERED -> getString(R.string.delivered)
                    else -> {
                        ""
                    }

                }
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarOptionImg.gone()
                loadOrdersData()
            }
        }
    }


    private fun loadOrdersData() {
        viewModel.ordersData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        ordersList.hideShimmer()
                        it.data?.let { response ->
                            response.data?.let { orders -> setUpList(orders) }
                        }
                    }

                    is NetworkStatus.Error -> {
                        ordersList.hideShimmer()

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        ordersList.showShimmer()
                    }

                }
            }
        }


    }

     private fun setUpList(list: List<ResponseOrders.Data>) {
         binding.apply {
             ordersAdapter.setData(list)
            ordersList.initRecyclerViewAdapter(ordersAdapter)

         }
     }
}