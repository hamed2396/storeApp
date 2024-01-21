package com.example.storeapp.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.crazylegend.kotlinextensions.context.color
import com.crazylegend.kotlinextensions.ifNotNull
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.isNotNull
import com.crazylegend.kotlinextensions.isNull
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.visible
import com.example.storeapp.R
import com.example.storeapp.data.stored.SessionManger
import com.example.storeapp.databinding.ActivityMainBinding
import com.example.storeapp.utils.events.Event
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.network.NetworkChecker
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.utils.otp.AppSignatureHelper
import com.example.storeapp.utils.otp.SmsBroadcastReceiver
import com.example.storeapp.viewModels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment }
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModels<CartViewModel>()

    @Inject
    lateinit var appSignatureHelper: AppSignatureHelper

    @Inject
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    @Inject
    lateinit var checker: NetworkChecker

    @Inject
    lateinit var sessionManager: SessionManger
    var hashCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            //update badge
            lifecycleScope.launch {
                EventBus.subscribe<Event.IsUpdateCart>(this) {
                    viewModel.callGetCartListApi()
                }

            }

            //check if users has already signed up. if not dont send to server
            lifecycleScope.launch {
                sessionManager.getToken().collect {
                    it.ifNotNull {
                        //when user enters app the badge should be updated regardless of changing the event
                        checker.checkNetwork().collect { internet ->
                            internet.ifTrue {
                                viewModel.callGetCartListApi()
                            }
                        }
                    }
                    it.isNull
                }
            }
            loadCartData()

            bottomNav.apply {
                setupWithNavController(navHostFragment.navController)
                setOnItemReselectedListener { }
            }
            navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

                when (destination.id) {
                    R.id.categoryFragment3 -> {
                        bottomNav.visible()
                    }

                    R.id.homeFragment3 -> {
                        bottomNav.visible()
                    }

                    R.id.cartFragment3 -> {
                        bottomNav.visible()
                    }

                    R.id.profileFragment3 -> {
                        bottomNav.visible()
                    }

                    else -> {
                        bottomNav.gone()
                    }
                }
            }

            //generate hashCode
            appSignatureHelper.appSignatures.forEach {
                hashCode = it


            }


        }
    }

    private fun loadCartData() {
        viewModel.cartData.observe(this) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        "ccc".logError()
                        if (it.data?.quantity.isNotNull) {
                            if (it.data?.quantity!!.toInt() > 0) {
                                bottomNav.getOrCreateBadge(R.id.cartFragment3).apply {
                                    number = it.data.quantity.toInt()
                                    backgroundColor = color(R.color.caribbeanGreen)
                                }
                            } else {
                                bottomNav.removeBadge(R.id.cartFragment3)
                            }

                        } else {
                            bottomNav.removeBadge(R.id.cartFragment3)
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


    override fun onNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() or super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


}