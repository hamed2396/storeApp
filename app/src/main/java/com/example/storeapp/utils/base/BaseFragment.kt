package com.example.storeapp.utils.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.fragments.longToast
import com.crazylegend.kotlinextensions.ifFalse
import com.crazylegend.kotlinextensions.log.wtf
import com.example.storeapp.R
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.network.NetworkChecker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes val fragment: Int) : Fragment(fragment) {
    @Inject
    lateinit var networkChecker: NetworkChecker
    protected var isNetworkAvailable = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchIoLifeCycle {
            networkChecker.checkNetwork().collect {
                isNetworkAvailable = it
                it.ifFalse {
                    longToast(R.string.checkYourNetwork)
                }
            }
        }
    }


}