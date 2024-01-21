package com.example.storeapp.ui.address

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.ifNotNull
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.DialogDeleteAddressBinding
import com.example.storeapp.databinding.FragmentProfileAddressAddBinding
import com.example.storeapp.models.address.BodyAddresses
import com.example.storeapp.models.address.ResponseAddresses
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.changeTint
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.AddressesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AddAddressFragment : BaseFragment(R.layout.fragment_profile_address_add) {
    private val binding by viewBinding(FragmentProfileAddressAddBinding::bind)
    private val viewModel by viewModels<AddressesViewModel>()

    private val provincesName = mutableListOf<String>()
    private var provincesId = 0
    private lateinit var provincesAdapter: ArrayAdapter<String>
    private val cityName = mutableListOf<String>()
    private lateinit var cityAdapter: ArrayAdapter<String>

    @Inject
    lateinit var body: BodyAddresses
    private val args: AddAddressFragmentArgs by navArgs()
    private lateinit var argsData: ResponseAddresses.ResponseAddressesItem
    private var addressId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.let {
                (it.address as? ResponseAddresses.ResponseAddressesItem).ifNotNull {
                    argsData = this
                    fillAddress(this)
                    true
                }


            }
            loadProvinceData()
            loadCityData()
            loadSubmitAddressData()
            loadDeleteAddressData()

            isNetworkAvailable.ifTrue {
                viewModel.callProvinceApi()
            }
            toolbar.apply {
                if (::argsData.isInitialized) {
                    toolbarTitleTxt.text = getString(R.string.editAddress)
                    toolbarOptionImg.apply {
                        setImageResource(R.drawable.trash_can)
                        changeTint(R.color.red)
                        setOnClickListener { showDeleteAddressDialog() }

                    }
                } else {
                    toolbarTitleTxt.text = getString(R.string.addNewAddress)
                    toolbarOptionImg.gone()
                }

                toolbarBackImg.setOnClickListener { findNavController().popBackStack() }
                //submit
                submitBtn.setOnClickListener {
                    isNetworkAvailable.ifTrue {
                        body.receiverFirstname = nameEdt.textString
                        body.receiverLastname = familyEdt.textString
                        body.receiverCellphone = phoneEdt.textString
                        body.floor = floorEdt.textString
                        body.plateNumber = plateEdt.textString
                        body.postalAddress = addressEdt.textString
                        body.postalCode = postalEdt.textString
                        viewModel.callSubmitAddressDataApi(body)
                    }
                }

            }

        }
    }

    private fun loadProvinceData() {
        viewModel.provinceData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        it.data?.let { response ->

                            if (response.isNotEmpty()) {
                                provincesName.clear()
                                response.forEach {
                                    it.title?.let { province ->
                                        provincesName.add(
                                            province
                                        )
                                    }
                                }
                                provincesAdapter = ArrayAdapter(
                                    requireContext(),
                                    R.layout.dropdown_menu_popup_item, provincesName
                                )

                                provinceAutoTxt.apply {
                                    setAdapter(provincesAdapter)

                                    setOnItemClickListener { _, _, position, _ ->
                                        provincesId = response[position].id!!
                                        body.provinceId = provincesId.toString()

                                        isNetworkAvailable.ifTrue {
                                            viewModel.callCityApi(
                                                provincesId
                                            )
                                        }
                                    }
                                }
                            }
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

    private fun loadCityData() {
        viewModel.cityData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        it.data?.let { response ->
                            cityInpLay.visible()
                            if (response.isNotEmpty()) {
                                cityName.clear()
                                response.forEach { it.title?.let { it1 -> cityName.add(it1) } }
                                cityAdapter = ArrayAdapter(
                                    requireContext(),
                                    R.layout.dropdown_menu_popup_item, cityName
                                )
                                cityAutoTxt.apply {
                                    setAdapter(cityAdapter)
                                    setOnItemClickListener { _, _, position, _ ->
                                        body.cityId = response[position].id.toString()
                                    }

                                }
                            }
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

    private fun loadDeleteAddressData() {
        viewModel.deleteAddressData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        it.data?.let {
                            findNavController().popBackStack()
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

    private fun loadSubmitAddressData() {
        viewModel.submitAddressData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        submitBtn.enableLoading(false)
                        it.data?.let {
                            findNavController().popBackStack()
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


    private fun fillAddress(data:ResponseAddresses.ResponseAddressesItem) {
        binding.apply {
            nameEdt.setText(data.receiverFirstname)
            familyEdt.setText(data.receiverLastname)
            phoneEdt.setText(data.receiverCellphone)
            floorEdt.setText(data.floor)
            plateEdt.setText(data.plateNumber)
            postalEdt.setText(data.postalCode)
            addressEdt.setText(data.postalAddress)
            provinceAutoTxt.setText(data.province?.title)
            binding.cityInpLay.visible()
            cityAutoTxt.setText(data.city?.title)
            body.provinceId = data.province?.id.toString()
            body.cityId = data.city?.id.toString()
            addressId = data.id!!
            body.addressId = addressId.toString()


        }
    }

    private fun showDeleteAddressDialog() {
        binding.apply {
            val dialogBinding = DialogDeleteAddressBinding.inflate(layoutInflater)
            Dialog(requireContext()).apply {
                transparentCorners()
                setContentView(dialogBinding.root)
                dialogBinding.yesBtn.setOnClickListener {
                    dismiss()
                    viewModel.callDeleteAddressDataApi(addressId)
                }
                dialogBinding.noBtn.setOnClickListener {

                    dismiss()
                }
                show()
            }


        }
    }

}