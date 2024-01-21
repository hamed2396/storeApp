package com.example.storeapp.ui.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.cart.CartAdapter
import com.example.storeapp.databinding.FragmentCartBinding
import com.example.storeapp.models.cart.ResponseCart
import com.example.storeapp.ui.MainActivity
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.events.Event
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.formatWithCommas
import com.example.storeapp.utils.extensions.isBiggerThan
import com.example.storeapp.utils.extensions.isLessThan
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.observeEvent
import com.example.storeapp.viewModels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment(R.layout.fragment_cart) {
    private val binding by viewBinding(FragmentCartBinding::bind)
    private val viewModel by viewModels<CartViewModel>()

    @Inject
    lateinit var cartAdapter: CartAdapter
    private var restoreRecyclerState: Parcelable? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            isNetworkAvailable.ifTrue { viewModel.callGetCartListApi() }
            loadCartData()
            loadCartUpdateData()
            loadCartContinueData()

            cartsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if ((dy isBiggerThan 0) and (continueFABtn.isExtended)) {
                        continueFABtn.shrink()

                    } else if ((dy isLessThan 0) and (continueFABtn.isExtended).not()) {
                        continueFABtn.extend()
                    }
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCartData() {
        viewModel.cartData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        loading.gone()
                        it.data?.let { response ->
                            if (response.orderItems.isNotNullOrEmpty) {
                                emptyLay.gone()
                                cartsList.visible()
                                continueFABtn.visible()
                                continueFABtn.setOnClickListener { isNetworkAvailable.ifTrue { viewModel.callGetCartContinueApi() } }
                                initCartRecyclerView(response.orderItems!!)
                                //toolbar text
                                toolbarPriceTxt.text =response.itemsPrice?.toInt()?.formatWithCommas()

                            } else {
                                emptyLay.visible()
                                cartsList.gone()
                                continueFABtn.gone()
                                toolbarPriceTxt.text = "$0 ${getString(R.string.toman)}"
                                launchIoLifeCycle {
                                    EventBus.publish(Event.IsUpdateCart)
                                }
                            }

                        }

                    }

                    is NetworkStatus.Error -> {
                        loading.gone()

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        loading.visible()


                    }

                }
            }


        }

    }

    private fun loadCartUpdateData() {
        viewModel.cartUpdateData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        loading.isVisible(false, cartsList)
                        it.data?.let {
                            isNetworkAvailable.ifTrue {
                                viewModel.callGetCartListApi()
                                launchIoLifeCycle {
                                    EventBus.publish(Event.IsUpdateCart)
                                }
                            }

                        }

                    }

                    is NetworkStatus.Error -> {
                        loading.isVisible(false, cartsList)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        loading.isVisible(true, cartsList)


                    }

                }
            }


        }

    }

    private fun initCartRecyclerView(list: List<ResponseCart.OrderItem>) {
        cartAdapter.setData(list)
        binding.cartsList.initRecyclerViewAdapter(cartAdapter)
        binding.cartsList.layoutManager?.onRestoreInstanceState(restoreRecyclerState)
        cartAdapter.setOnItemClickListener { id: Int, type: String ->
            when (type) {
                Constants.INCREMENT -> {
                    isNetworkAvailable.ifTrue { viewModel.callPutCartIncrementDataApi(id) }

                }

                Constants.DECREMENT -> {
                    isNetworkAvailable.ifTrue { viewModel.callPutCartDecrementDataApi(id) }
                }

                Constants.DELETE -> {
                    isNetworkAvailable.ifTrue { viewModel.callDeleteProductDataApi(id) }
                }
            }

        }


    }

    override fun onStop() {
        super.onStop()
        restoreRecyclerState = binding.cartsList.layoutManager?.onSaveInstanceState()
    }

    private fun loadCartContinueData() {
        viewModel.cartContinueData.observeEvent(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        loading.isVisible(false, cartsList)
                        it.data?.let {
                            findNavController().navigate(R.id.actionToShipment)

                        }

                    }

                    is NetworkStatus.Error -> {
                        loading.isVisible(false, cartsList)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        loading.isVisible(true, cartsList)


                    }

                }
            }


        }

    }


}