package com.example.storeapp.ui.login

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.insets.hideKeyboard
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.toggleVisibilityGoneToVisible
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.data.stored.SessionManger
import com.example.storeapp.databinding.FragmentLoginBinding
import com.example.storeapp.databinding.FragmentLoginVerifyBinding
import com.example.storeapp.models.login.BodyLogin
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.observeEvent
import com.example.storeapp.utils.otp.SmsBroadcastReceiver
import com.example.storeapp.viewModels.LoginViewModel
import com.goodiebag.pinview.Pinview
import com.google.android.gms.auth.api.phone.SmsRetriever
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class VerifyFragment : BaseFragment(R.layout.fragment_login_verify) {
    private val binding by viewBinding(FragmentLoginVerifyBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var body: BodyLogin
    private val args by navArgs<VerifyFragmentArgs>()

    @Inject
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    private var intentFilter: IntentFilter? = null

    @Inject
    lateinit var sessionManager: SessionManger

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Constants.IS_CALLED_VERIFIED = false
        args.let {
            body.login = it.phone
        }
        binding.apply {
            bottomImg.load(R.drawable.bg_circle)
            pinView.setTextColor(compatColor(R.color.white))
            pinView.setPinViewEventListener(object : Pinview.PinViewEventListener {
                override fun onDataEntered(pinview: Pinview?, fromUser: Boolean) {
                    body.code = pinview?.value?.toInt()
                    isNetworkAvailable.ifTrue { viewModel.callVerifyApi(body) }

                }
            })
            sendAgainBtn.setOnClickListener {
                isNetworkAvailable.ifTrue {
                    viewModel.callLoginApi(body)
                    handleTimer().cancel()
                }
            }

        }
        initBroadCast()
        smsListener()
        showAnimation()
        loadLoginData()
        loadVerifyData()
        launchIoLifeCycle {
            delay(250)
            handleTimer().start()
        }

    }

    private fun handleTimer(): CountDownTimer {
        binding.apply {
            return object : CountDownTimer(60_000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millis: Long) {
                    sendAgainBtn.gone()
                    timerTxt.visible()
                    timerProgress.visible()
                    timerTxt.text = "${millis.div(1000)} ${getString(R.string.second)}"
                    timerProgress.setProgressCompat((millis.div(1000).toInt()), true)
                    if (millis < 1000) timerProgress.progress = 0
                }

                override fun onFinish() {
                    sendAgainBtn.toggleVisibilityGoneToVisible()
                    timerTxt.toggleVisibilityGoneToVisible()
                    timerProgress.toggleVisibilityGoneToVisible()
                    timerProgress.progress = 0

                }
            }
        }
    }

    private fun loadLoginData() {
        viewModel.loginData.observeEvent(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        sendAgainBtn.enableLoading(false)
                        it.data?.let {
                            handleTimer().start()
                        }
                    }

                    is NetworkStatus.Error -> {
                        sendAgainBtn.enableLoading(false)
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        sendAgainBtn.enableLoading(true)

                    }

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


    private fun initBroadCast() {
        intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        smsBroadcastReceiver.onReceiveMessage {
            val code = it.split(":")[1].trim().subSequence(0, 4)
            binding.pinView.value = code.toString()
        }
    }

    private fun smsListener() {
        SmsRetriever.getClient(requireContext()).apply {
            startSmsRetriever()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(smsBroadcastReceiver, intentFilter)

    }

    override fun onStop() {
        super.onStop()
        handleTimer().cancel()
        requireContext().unregisterReceiver(smsBroadcastReceiver)
    }

    private fun loadVerifyData() {
        viewModel.verifyData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        timerLay.alpha = 1f
                        it.data?.let {
                            launchIoLifeCycle {
                                sessionManager.saveToken(it.accessToken!!)
                            }
                            root.hideKeyboard()
                            findNavController().popBackStack(R.id.verifyFragment, true)
                            findNavController().popBackStack(R.id.loginFragment, true)
                            findNavController().popBackStack(R.id.splashFragment, true)
                            findNavController().navigate(R.id.actionToMain)

                        }
                    }

                    is NetworkStatus.Error -> {
                        timerLay.alpha = 1f
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {

                        timerLay.alpha = 0.3f
                    }

                }
            }
        }

    }


}