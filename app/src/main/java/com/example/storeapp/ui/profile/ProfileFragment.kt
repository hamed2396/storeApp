package com.example.storeapp.ui.profile

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.app.imagepickerlibrary.ImagePicker.Companion.registerImagePicker
import com.app.imagepickerlibrary.listener.ImagePickerResultListener
import com.app.imagepickerlibrary.model.PickExtension
import com.app.imagepickerlibrary.model.PickerType
import com.crazylegend.kotlinextensions.file.getRealPath
import com.crazylegend.kotlinextensions.file.toFile
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.kotlinextensions.whether
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentProfileBinding
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.events.Event
import com.example.storeapp.utils.events.EventBus

import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.formatWithCommas
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.ProfileViewModel
import com.example.storeapp.viewModels.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.net.URLEncoder


@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile), ImagePickerResultListener {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by activityViewModels<ProfileViewModel>()
    private val walletViewModel by viewModels<WalletViewModel>()
    private val imagePicker by lazy { registerImagePicker(this) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            //auto update profile
            launchIoLifeCycle {
                EventBus.subscribe<Event.IsUpdateProfile>(this) {
                    viewModel.callProfileApi()
                }

            }

            loadProfileData()
            loadWalletData()
            loadAvatarData()
            avatarEditImg.setOnClickListener {
                openImagePicker()
            }
            //menu items
            menuLay.apply {
                menuEditLay.setOnClickListener { findNavController().navigate(R.id.actionToEditProfile) }
                menuWalletLay.setOnClickListener { findNavController().navigate(R.id.actionToIncreaseWallet) }
                menuCommentsLay.setOnClickListener { findNavController().navigate(R.id.actionToProfileComments) }
                menuFavoritesLay.setOnClickListener { findNavController().navigate(R.id.actionToProfileFavorite) }
                menuAddressesLay.setOnClickListener { findNavController().navigate(R.id.actionToProfileAddress) }
                orderLay.menuDeliveredLay.setOnClickListener {
                    val direction=ProfileFragmentDirections.actionToProfileOrders(Constants.DELIVERED)
                    findNavController().navigate(direction)
                }
                orderLay.menuPendingLay.setOnClickListener {
                    val direction=ProfileFragmentDirections.actionToProfileOrders(Constants.PENDING)
                    findNavController().navigate(direction)
                }
                orderLay.menuCanceledLay.setOnClickListener {
                    val direction=ProfileFragmentDirections.actionToProfileOrders(Constants.CANCELED)
                    findNavController().navigate(direction)
                }


            }



        }

    }


    @SuppressLint("SetTextI18n")
    private fun loadProfileData() {
        viewModel.profileData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        loading.isVisible(false, scrollLay)
                        it.data?.let { response ->

                            (response.avatar != null).whether({
                                avatarImg.loadImage(response.avatar!!)

                            }, {

                                avatarImg.load(R.drawable.placeholder_user)

                            })
                            //name
                            if (response.firstname.isNullOrEmpty().not()) nameTxt.text =
                                "${response.firstname} ${response.lastname}"
                            //info mobile
                            if (response.cellphone.isNullOrEmpty().not()) {
                                infoLay.phoneTxt.text = response.cellphone!!
                            }
                            //info birthDate
                            if (response.birthDate.isNullOrEmpty().not()) {
                                infoLay.infoBirthDateLay.visible()
                                infoLay.line2.visible()
                                infoLay.birthDateTxt.text =
                                    response.birthDate!!.split("T")[0].replace("-", " / ")
                            } else {
                                infoLay.infoBirthDateLay.gone()
                                infoLay.line2.gone()
                            }

                        }
                    }

                    is NetworkStatus.Error -> {
                        loading.isVisible(false, scrollLay)
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        loading.isVisible(true, scrollLay)

                    }

                }
            }
        }

    }

    private fun loadAvatarData() {
        viewModel.avatarData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        avatarLoading.gone()
                        isNetworkAvailable.ifTrue { viewModel.callProfileApi() }
                    }

                    is NetworkStatus.Error -> {
                        avatarLoading.gone()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        avatarLoading.visible()

                    }

                }
            }
        }

    }

    private fun loadWalletData() {
        walletViewModel.walletData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        infoLay.walletLoading.gone()
                        it.data?.let { response ->
                            if (response.wallet.isNullOrEmpty().not()) {
                                infoLay.walletTxt.text =
                                    response.wallet!!.toInt().formatWithCommas()
                            }


                        }
                    }

                    is NetworkStatus.Error -> {
                        infoLay.walletLoading.gone()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {

                        infoLay.walletLoading.visible()

                    }

                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        walletViewModel.callWalletApi()
    }

    private fun openImagePicker() {
        imagePicker
            .title("My Picker")
            .multipleSelection(enable = false)
            .showCountInToolBar(false)
            .showFolder(true)
            .cameraIcon(true)
            .doneIcon(true)
            .allowCropping(true)
            .compressImage(false)
            .maxImageSize(2.5f)
            .extension(PickExtension.ALL)
        imagePicker.open(PickerType.GALLERY)
    }

    override fun onImagePick(uri: Uri?) {

        // Attempt to get the file from the URI
        uri?.getRealPath(requireContext())?.toFile()?.let { imageFile ->

            // Create the MultipartBody for the API request
            val requestBody = createMultipartBody(imageFile)

            isNetworkAvailable.takeIf { it }?.let {
                viewModel.callUploadAvatarApi(requestBody)
            }
        }


    }

    // Function to create MultipartBody with form data
    private fun createMultipartBody(imageFile: File): RequestBody {
        return MultipartBody.Builder().apply {
            setType(MultipartBody.FORM)
            addFormDataPart(Constants.METHOD, Constants.POST)
            addImageFormDataPart(Constants.AVATAR, imageFile)

        }.build()
    }

    // Extension function to add image form data part to MultipartBody
    private fun MultipartBody.Builder.addImageFormDataPart(
        name: String,
        imageFile: File
    ): MultipartBody.Builder {
        // Encode the file name and add it to the form data
        val fileName = URLEncoder.encode(imageFile.absolutePath, Constants.UTF8)
        val reqFile = imageFile.asRequestBody(Constants.MULTIPART_FORM_DATA.toMediaTypeOrNull())
        return addFormDataPart(name, fileName, reqFile)
    }


    override fun onMultiImagePick(uris: List<Uri>?) {
    }


}