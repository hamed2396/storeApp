package com.example.storeapp.ui.profile.edtProfile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.setOnSingleTapListener
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentEditProfileBinding
import com.example.storeapp.models.profile.BodyUpdateProfile
import com.example.storeapp.utils.events.Event
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.api.PersianPickerDate
import ir.hamsaa.persiandatepicker.api.PersianPickerListener
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : BottomSheetDialogFragment(R.layout.fragment_edit_profile) {

    private val binding by viewBinding(FragmentEditProfileBinding::bind)
    private val profileViewModel by viewModels<ProfileViewModel>()

    @Inject
    lateinit var body: BodyUpdateProfile

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            loadEditProfileData()
            loadUpdateProfileData()
            closeImg.setOnClickListener { dismiss() }
            birthDateEdt.setOnSingleTapListener { _, _ -> openDatePicker() }
       /*     birthDateEdt.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    openDatePicker()

                }
                false
            }*/
            submitBtn.setOnClickListener {
                if (nameEdt.text.isNullOrEmpty().not()) {
                    body.firstName = nameEdt.textString
                }
                if (familyEdt.text.isNullOrEmpty().not()) {
                    body.lastName = familyEdt.textString
                }
                if (idNumberEdt.text.isNullOrEmpty().not()) {
                    body.idNumber = idNumberEdt.textString
                }
                profileViewModel.callUpdateProfileApi(body)
            }


        }

    }

    private fun loadEditProfileData() {
        profileViewModel.callProfileApi()
        profileViewModel.profileData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        loading.gone()

                        it.data?.let { response ->
                            if (response.firstname.isNullOrEmpty().not()) {
                                nameEdt.setText(response.firstname)
                            }
                            if (response.lastname.isNullOrEmpty().not()) {
                                familyEdt.setText(response.lastname)
                            }
                            if (response.birthDate.isNullOrEmpty().not()) {
                                birthDateEdt.setText(response.birthDate.toString().split("T")[0])
                            }
                            response.idNumber?.let {
                                if ((it as String).isNotEmpty()) {
                                    idNumberEdt.setText(it)
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

    private fun loadUpdateProfileData() {

        profileViewModel.updateProfileData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        submitBtn.enableLoading(false)

                        it.data?.let {
                            dismiss()
                            launchIoLifeCycle {
                                EventBus.publish(Event.IsUpdateProfile)
                            }


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

    private fun openDatePicker() {
        PersianDatePickerDialog(requireContext())
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setMaxYear(1400)
            .setInitDate(1370, 3, 13)
            .setTitleType(PersianDatePickerDialog.DAY_MONTH_YEAR)
            .setShowInBottomSheet(true)
            .setListener(object : PersianPickerListener {
                override fun onDateSelected(persianDate: PersianPickerDate) {
                    val birthDate =
                        "${persianDate.gregorianYear}-${persianDate.gregorianMonth}-${persianDate.gregorianDay}"
                    val birthDatePersian =
                        "${persianDate.persianYear} / ${persianDate.persianMonth} / ${persianDate.persianDay}"
                    body.gregorianDate = birthDate
                    binding.birthDateEdt.setText(birthDatePersian)
                }

                override fun onDismissed() {}
            }).show()
    }

    override fun getTheme(): Int {
        return R.style.RemoveDialogBackground
    }

}