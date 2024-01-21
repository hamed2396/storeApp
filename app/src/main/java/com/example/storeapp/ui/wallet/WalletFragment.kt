package com.example.storeapp.ui.wallet

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.views.snackbar
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentIncreaseWalletBinding

import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.formatWithCommas
import com.example.storeapp.utils.extensions.openBrowser
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.WalletViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WalletFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentIncreaseWalletBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WalletViewModel>()

    @Inject
    lateinit var body: BodyIncreaseWallet


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncreaseWalletBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            closeImg.setOnClickListener { dismiss() }
            loadWalletData()
            amountEdt.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    amountTxt.text = it.toString().trim().toInt().formatWithCommas()
                } else {
                    amountTxt.text = ""
                }
            }
            submitBtn.setOnClickListener {
                val amount = amountEdt.text.toString()
                if (amount.isNotEmpty()) {
                    body.amount = amount
                    viewModel.callIncreaseWalletApi(body)
                }
            }


        }
    }

    private fun loadWalletData() {
        viewModel.increaseWallet.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        submitBtn.enableLoading(false)
                        it.data?.let { response ->

                            Uri.parse(response.startPay).openBrowser(requireContext())
                            dismiss()


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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun getTheme(): Int {
        return R.style.RemoveDialogBackground
    }

}