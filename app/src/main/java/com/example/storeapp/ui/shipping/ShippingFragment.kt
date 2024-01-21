package com.example.storeapp.ui.shipping

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.shipping.ShippingAdapter
import com.example.storeapp.adapters.shipping.ShippingAddressesAdapter
import com.example.storeapp.databinding.DialogChangeAddressBinding
import com.example.storeapp.databinding.FragmentShippingBinding
import com.example.storeapp.models.shipping.BodyCheckCoupon
import com.example.storeapp.models.shipping.BodyUpdateAddress
import com.example.storeapp.models.shipping.ResponseShipment
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.formatWithCommas
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.openBrowser
import com.example.storeapp.utils.extensions.toEditable
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.ShippingViewModel
import com.example.storeapp.viewModels.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class ShippingFragment : BaseFragment(R.layout.fragment_shipping) {
    private val binding by viewBinding(FragmentShippingBinding::bind)
    private val viewModel by viewModels<ShippingViewModel>()
    private var finalPrice = 0

    @Inject
    lateinit var shippingAdapter: ShippingAdapter

    @Inject
    lateinit var shippingAddressesAdapter: ShippingAddressesAdapter

    @Inject
    lateinit var body: BodyUpdateAddress

    @Inject
    lateinit var bodyCoupon: BodyCheckCoupon
    private val walletViewModel by viewModels<WalletViewModel>()
    private var coupon = ""


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            isNetworkAvailable.ifTrue {
                viewModel.callShippingApi()
                walletViewModel.callWalletApi()
            }
            shippingDiscountLay.apply {
                checkTxt.setOnClickListener {
                    coupon = codeEdt.text.toString()
                    bodyCoupon.couponId = coupon
                    isNetworkAvailable.ifTrue {
                        viewModel.callPostCheckCouponApi(bodyCoupon)
                    }

                }
                //auto scroll
              codeEdt.setOnTouchListener { v, _ ->
                  v.performClick()
                 lifecycleScope.launch {
                     delay(300)
                     binding.scrollLay.fullScroll(View.FOCUS_DOWN)
                 }
                  false
              }


            }
            toolbar.apply {
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarTitleTxt.text = getString(R.string.invoiceWithDeliveryPrice)
                toolbarOptionImg.gone()
            }

            isNetworkAvailable.ifTrue { viewModel.callShippingApi() }
            loadShippingData()
            loadWalletData()
            loadCouponData()
            loadPaymentData()


        }
    }

    private fun loadWalletData() {
        walletViewModel.walletData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        it.data?.let { response ->
                            loading.gone()
                            walletTxt.gone()
                            walletTxt.text = response.wallet.toString().toInt().formatWithCommas()
                        }
                    }

                    is NetworkStatus.Error -> {
                        loading.gone()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        walletTxt.visible()
                        loading.visible()
                    }

                }
            }
        }

    }

    private fun loadShippingData() {
        viewModel.shippingData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        walletLoading.isVisible(false, containerGroup)
                        it.data?.let {
                            initShippingViews(it)

                        }

                    }

                    is NetworkStatus.Error -> {
                        walletLoading.isVisible(false, containerGroup)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        walletLoading.isVisible(true, containerGroup)


                    }

                }
            }


        }

    }

    private fun loadCouponData() {
        viewModel.checkCouponData.observe(viewLifecycleOwner) {
            binding.shippingDiscountLay.apply {
                when (it) {
                    is NetworkStatus.Data -> {

                        it.data?.let { response ->
                            couponLoading.isVisible(false, checkTxt)
                            checkTxt.gone()
                            removeTxt.visible()
                            couponTitle.text =
                                "${getString(R.string.discountCode)} (${response.title})"
                            // status
                            if (response.status == Constants.ENABLE) {
                                coupon = response.code!!
                                bodyCoupon.couponId = coupon
                                val discountPrice = if (response.type == Constants.PERCENT) {
                                    finalPrice.minus(
                                        finalPrice.times(response.percent!!.toInt()).div(100)
                                    )

                                } else {
                                    finalPrice.minus(response.percent!!.toInt())
                                }
                                binding.invoiceTitle.text = discountPrice.formatWithCommas()
                                //remove
                                removeTxt.setOnClickListener {
                                    checkTxt.visible()
                                    removeTxt.gone()

                                    codeEdt.text = "".toEditable
                                    couponTitle.text = getString(R.string.discountCode)
                                    coupon = ""
                                    bodyCoupon.couponId = null
                                    binding.invoiceTitle.text = finalPrice.formatWithCommas()
                                }
                            }
                        }

                    }

                    is NetworkStatus.Error -> {
                        bodyCoupon.couponId = null
                        couponLoading.isVisible(false, checkTxt)
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        couponLoading.isVisible(true, checkTxt)

                    }

                }
            }


        }

    }

    private fun initShippingViews(data: ResponseShipment) {
        binding.apply {
            //recycler view orders
            data.order?.let {
                finalPrice = it.finalPrice!!.toInt()
                if (it.orderItems.isNotNullOrEmpty) {
                    initCartRecyclerView(it.orderItems!!)
                }
            }
            //addresses
            if (data.addresses.isNotNullOrEmpty) {
                data.addresses?.get(0)?.let {
                    setAddressData(it)
                }
                if (data.addresses!!.size > 1) {
                    shippingAddressLay.changeAddressTxt.apply {
                        visible()
                        setOnClickListener {
                            showChangeAddressDialog(data.addresses)
                        }
                    }
                }

            }
            submitBtn.setOnClickListener {
                if (data.addresses.isNotNullOrEmpty) {
                    isNetworkAvailable.ifTrue { viewModel.callPostPaymentApi(bodyCoupon) }
                } else {
                    root.snackbar(getString(R.string.addressCanNotBeEmpty))
                }
            }


        }
    }


    private fun setAddressData(it: ResponseShipment.Addresse) {
        body.addressId = it.id!!.toString()
        binding.shippingAddressLay.apply {
            recipientNameTxt.text = "${it.receiverFirstname} ${it.receiverLastname}"
            locationTxt.text = it.postalAddress
            phoneTxt.text = it.receiverCellphone
        }
        //cal set address
        isNetworkAvailable.ifTrue { viewModel.callPutUpdateAddressApi(body) }
    }


    private fun showChangeAddressDialog(list: List<ResponseShipment.Addresse>) {
        val dialog = Dialog(requireContext())
        val dialogBinding = DialogChangeAddressBinding.inflate(layoutInflater)
        dialog.transparentCorners()
        dialog.setContentView(dialogBinding.root)
        shippingAddressesAdapter.setData(list)
        dialogBinding.addressList.initRecyclerViewAdapter(shippingAddressesAdapter)
        shippingAddressesAdapter.setOnItemClickListener {
            setAddressData(it)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initCartRecyclerView(list: List<ResponseShipment.Order.OrderItem>) {
        shippingAdapter.setData(list)
        binding.productsList.initRecyclerViewAdapter(shippingAdapter)

    }

    private fun loadPaymentData() {
        viewModel.paymentData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        submitBtn.enableLoading(false)
                        it.data?.let { response ->
                            response.startPay.logError()
                            Uri.parse(response.startPay).openBrowser(requireContext())
                            findNavController().popBackStack(R.id.shippingFragment, true)


                        }
                    }

                    is NetworkStatus.Error -> {
                        submitBtn.enableLoading(false)
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {

                        submitBtn.enableLoading(true)

                    }

                }
            }
        }

    }


}