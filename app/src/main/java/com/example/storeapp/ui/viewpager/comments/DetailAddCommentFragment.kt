package com.example.storeapp.ui.viewpager.comments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.viewModels
import com.crazylegend.kotlinextensions.context.snackBar
import com.crazylegend.kotlinextensions.fragments.rootView
import com.crazylegend.kotlinextensions.tryOrElse
import com.crazylegend.kotlinextensions.tryOrPrint
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentAddCommentBinding
import com.example.storeapp.models.details.BodyAddComment
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.DetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailAddCommentFragment : BottomSheetDialogFragment(R.layout.fragment_add_comment) {

    private val binding by viewBinding(FragmentAddCommentBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()

    @Inject
    lateinit var body: BodyAddComment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            closeImg.setOnClickListener { dismiss() }
            submitBtn.setOnClickListener {
                val rate = rateSlider.value.toInt().toString()
                val comment = commentEdt.textString
                body.comment = comment
                body.rate = rate 
                viewModel.callPostCommentApi(Constants.PRODUCT_ID, body)
            }
            loadAddProductCommentData()

        }


    }

    private fun loadAddProductCommentData() {
        viewModel.postCommentData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        submitBtn.enableLoading(false)
                        it.data?.message?.let {
                         parentFragment?.rootView?.snackbar(it)
                        }

                        dismiss()


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

    override fun getTheme() = R.style.RemoveDialogBackground


}