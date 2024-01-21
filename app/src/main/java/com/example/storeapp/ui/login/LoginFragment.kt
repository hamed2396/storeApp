package com.example.storeapp.ui.login

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.insets.hideKeyboard
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentLoginBinding
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.ui.MainActivity
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.observeEvent
import com.example.storeapp.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by activityViewModels<LoginViewModel>()
    private var phoneNumber = ""

    @Inject
    lateinit var body: BodyLogin
    private val parentActivity by lazy { (activity as MainActivity) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            showAnimation()
            loadLoginData()
            bottomImg.load(R.drawable.bg_circle)
            body.hashCode = parentActivity.hashCode
            sendPhoneBtn.setOnClickListener {
                root.hideKeyboard()
                phoneNumber = phoneEdt.text.toString()
                body.login = phoneEdt.text.toString()
                isNetworkAvailable.ifTrue {
                    viewModel.callLoginApi(body)
                }



            }
        }
    }

    private fun showAnimation() {
        binding.apply {
            animationView.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    launchIoLifeCycle {
                        delay(2000)
                        animationView.playAnimation()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {

                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
        }
    }



    private fun loadLoginData() {
        viewModel.loginData.observeEvent(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        sendPhoneBtn.enableLoading(false)
                        it.data?.let {
                            val direction =
                                LoginFragmentDirections.actionPhoneToVerify(phoneNumber)
                            findNavController().navigate(direction)


                        }
                    }

                    is NetworkStatus.Error -> {
                        sendPhoneBtn.enableLoading(false)
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        sendPhoneBtn.enableLoading(true)

                    }

                }
            }
        }

    }
}