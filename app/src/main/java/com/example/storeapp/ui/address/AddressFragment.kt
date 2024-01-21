package com.example.storeapp.ui.address

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.string.safe
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.addresses.AddressesAdapter
import com.example.storeapp.databinding.FragmentProfileAddressBinding
import com.example.storeapp.models.address.ResponseAddresses
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.AddressesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressFragment : BaseFragment(R.layout.fragment_profile_address) {
    private val binding by viewBinding(FragmentProfileAddressBinding::bind)
    private val viewModel by viewModels<AddressesViewModel>()

    @Inject
    lateinit var addressAdapter: AddressesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            toolbar.apply {
                isNetworkAvailable.ifTrue { viewModel.callAddressesApi() }
                loadDeleteFavoriteData()
                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                toolbarTitleTxt.text = getString(R.string.yourAddress)
                toolbarOptionImg.apply {
                    setImageResource(R.drawable.location_plus)
                    setOnClickListener {
                        findNavController().navigate(R.id.addAddressFragment)
                    }
                }

            }


        }
    }


    private fun loadDeleteFavoriteData() {
        viewModel.addressesData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        addressList.hideShimmer()
                        it.data?.let { response ->

                            setUpAddressList(response)

                        }
                    }

                    is NetworkStatus.Error -> {
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {}

                }
            }
        }


    }

    private fun setUpAddressList(list: List<ResponseAddresses.ResponseAddressesItem>) {
        binding.apply {
            addressAdapter.setData(list)
            addressList.initRecyclerViewAdapter(addressAdapter)

            addressAdapter.setOnItemClickListener {response->
                AddressFragmentDirections.actionToAddAddress(response)
                    .apply { findNavController().navigate(this) }
            }
        }
    }
}