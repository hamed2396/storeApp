package com.example.storeapp.ui.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.fragments.drawable
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.string.asColor
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.deleteLine
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.detail.ImagesAdapter
import com.example.storeapp.databinding.DialogImageBinding
import com.example.storeapp.databinding.FragmentDetailBinding
import com.example.storeapp.models.cart.BodyAddToCart
import com.example.storeapp.models.details.ResponseDetail
import com.example.storeapp.ui.viewpager.adapters.PagerAdapter
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.events.Event
import com.example.storeapp.utils.events.EventBus
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.changeTint
import com.example.storeapp.utils.extensions.enableLoading
import com.example.storeapp.utils.extensions.formatWithCommas
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.loadImageWithGlide
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.CartViewModel
import com.example.storeapp.viewModels.DetailViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
 class DetailFragment : BaseFragment(R.layout.fragment_detail) {

    private val binding by viewBinding(FragmentDetailBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()
    private val cartViewModel by viewModels<CartViewModel>()
    private val args by navArgs<DetailFragmentArgs>()
    private var productId = 0
    private var isNeededColor = false
    private var isAddedToCart = 0
    private lateinit var countDownTimer: CountDownTimer


    @Inject
    lateinit var imagesAdapter: ImagesAdapter

    @Inject
    lateinit var pagerAdapter: PagerAdapter

    @Inject
    lateinit var body: BodyAddToCart


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            args.productId.let {
                productId = it
                Constants.PRODUCT_ID = it


            }
            if (productId > 0) {
                isNetworkAvailable.ifTrue { viewModel.callDetailApi(productId) }
            }
            binding.detailHeaderLay.backImg.setOnClickListener { findNavController().popBackStack() }
            loadDetailData()
            loadFavoriteData()
            loadAddToCartData()


        }

    }

    private fun loadDetailData() {
        viewModel.detailData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        detailLoading.isVisible(false, containerLay)
                        it.data?.let { response ->
                            initDetailViews(response)

                        }

                    }

                    is NetworkStatus.Error -> {
                        detailLoading.isVisible(false, containerLay)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        detailLoading.isVisible(true, containerLay)


                    }

                }
            }
        }

    }

    private fun initDetailViews(data: ResponseDetail) {
        initHeaderView(data)
        initInfoView(data)
        initTimer(data)
        setUpViewpager()
        initDetailBottomView(data)

    }

    private fun initHeaderView(data: ResponseDetail) {
        data.image?.let { loadImage(it) }
        binding.detailHeaderLay.apply {
            if (data.description.isNotNullOrEmpty()) {
                productInfo.text = data.description!!.parseAsHtml()
            } else productInfo.gone()

            if (data.colors.isNullOrEmpty().not()) {
                isNeededColor = true
                setUpChip(data.colors!!.toMutableList())
            } else {
                isNeededColor = false
                line1.gone()
                colorsTitle.gone()
                colorsScroll.gone()
            }
            //favorite
            data.isAddToFavorite?.let { updateFavUi(it.toInt()) }
            favImg.setOnClickListener {
                isNetworkAvailable.ifTrue {
                    viewModel.callPostFavoriteApi(
                        productId
                    )
                }
            }
            //images
            if (data.images.isNotNullOrEmpty) {
                data.images!!.add(0, data.image!!)
                initImagesAdapter(data.images.toMutableList())
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun initInfoView(data: ResponseDetail) {
        binding.detailInfoLay.apply {
            if (data.brand != null) {
                brandTxt.text = data.brand.title?.fa
            } else {
                brandLay.gone()
                line1.gone()
            }
            if (data.guarantee.isNotNullOrEmpty()) {
                guaranteeTxt.text = data.guarantee
            } else {
                guaranteeLay.gone()
                line3.gone()
            }
            categoryTxt.text = data.category!!.title
            quantityTxt.text = "${data.quantity} ${getString(R.string.item)}"
            commentsTxt.text = "${data.quantity} ${getString(R.string.item)}"
            rateTxt.text = "${data.likesCount} ${getString(R.string.rate)}"
            //special
            specialTitle.isVisible = data.status == Constants.SPECIAL

        }
    }


    private fun loadImage(image: String) {
        val photo = "${Constants.BASE_URL_IMAGE}$image"
        binding.detailHeaderLay.productImg.apply {
            this.loadImageWithGlide(photo)
            setOnClickListener { showDialog(photo) }
        }

    }

    private fun showDialog(image: String) {
        binding.apply {
            val dialogBinding = DialogImageBinding.inflate(layoutInflater)
            Dialog(requireContext()).apply {
                transparentCorners()
                setContentView(dialogBinding.root)
                dialogBinding.productImg.loadImageWithGlide(image)
                show()

            }
        }


    }

    private fun setUpChip(list: MutableList<ResponseDetail.Color>) {

        list.forEach {
            val chip = Chip(requireContext()).apply {
                val drawable = ChipDrawable.createFromAttributes(
                    requireContext(), null, 0, R.style.DetailChipsBackground
                )
                setChipDrawable(drawable)
                val color = if (it.hexCode?.lowercase() == Constants.COLOR_WHITE) {
                    Constants.COLOR_BLACK
                } else {
                    it.hexCode!!.lowercase()
                }
                setTextColor(color.asColor)
                id = it.id!!
                text = it.title
                setTextAppearance(R.style.DetailChipsText)


            }
            binding.detailHeaderLay.colorsChipGroup.apply {
                addView(chip)
                chip.setOnCheckedChangeListener { _, isChecked ->

                    isChecked.ifTrue { body.colorId = chip.id.toString() }
                }
            }

        }

    }

    private fun updateFavUi(count: Int) {
        binding.detailHeaderLay.favImg.apply {
            if (count == 0) changeTint(R.color.gray) else changeTint(R.color.salmon)

        }
    }

    private fun initImagesAdapter(images: MutableList<String>) {
        imagesAdapter.setData(images)
        binding.detailHeaderLay.productImagesList.initRecyclerViewAdapter(
            imagesAdapter, fixedSize = true
        )
        imagesAdapter.setOnItemClickListener {
            loadImage(it)
        }
    }

    private fun initTimer(data: ResponseDetail) {
        binding.detailTimerLay.apply {
            if (data.discountedPrice?.toInt()!! > 0) {
                if (data.endTime.isNotNullOrEmpty()) {
                    val time = data.endTime!!
                    discountTimer(time)
                    countDownTimer.start()


                } else {
                    priceDiscountLay.gone()
                }
                binding.detailBottom.oldPriceTxt.apply {
                    text = data.productPrice?.toInt()?.formatWithCommas()
                    deleteLine()
                }
            } else {
                priceDiscountLay.gone()
            }
        }
    }

    private fun discountTimer(data: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())

        try {
            val date = formatter.parse(data)
            val currentTime = System.currentTimeMillis()

            // Calculate the time difference in milliseconds
            date?.time?.let {
                val timeDifference = it - currentTime

                // Handle the time difference as needed
                countDownTimer = object : CountDownTimer(timeDifference, 1000) {
                    override fun onTick(millis: Long) {
                        var timer = millis

                        val day: Long = TimeUnit.MILLISECONDS.toDays(timer)
                        timer -= TimeUnit.DAYS.toMillis(day)

                        val hours: Long = TimeUnit.MILLISECONDS.toHours(timer)
                        timer -= TimeUnit.HOURS.toMillis(hours)

                        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timer)
                        timer -= TimeUnit.MINUTES.toMillis(minutes)

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(timer)

                        binding.detailTimerLay.timerLay.apply {
                            dayTxt.text = day.toString()
                            hourTxt.text = hours.toString()
                            minuteTxt.text = minutes.toString()
                            secondTxt.text = seconds.toString()
                        }
                    }

                    override fun onFinish() {
                        binding.detailTimerLay.discountTimerContainer.gone()
                    }

                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()

        }
    }

    private fun setUpViewpager() {

        binding.detailPagerLay.apply {
            detailTabLayout.addTab(detailTabLayout.newTab().setText(getString(R.string.comments)))
            detailTabLayout.addTab(detailTabLayout.newTab().setText(getString(R.string.features)))
            detailTabLayout.addTab(detailTabLayout.newTab().setText(getString(R.string.priceChart)))
            //viewPager
            detailViewPager.adapter = pagerAdapter
            detailTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) detailViewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    // when not selected
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //when its selected and you click on it again

                }
            })
            //this is used when you need to swipe to change tabs and tabLayout should be aware and be changed from top
            detailViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    detailTabLayout.selectTab(detailTabLayout.getTabAt(position))
                }
            })/* disable swipe
             detailViewPager.isUserInputEnabled=false*/

        }
    }

    private fun updateAddToCartUi(btn: MaterialButton) {
        binding.apply {
            btn.setBackgroundColor(compatColor(R.color.royalBlue))
            btn.icon = drawable(R.drawable.cart_circle_check)
            btn.text = getString(R.string.existsInCart)
        }

    }

    private fun initDetailBottomView(data: ResponseDetail) {
        //exist in cart
        isAddedToCart = data.isAddToCart!!
        binding.detailBottom.apply {
            if (data.isAddToCart == 1) {
                updateAddToCartUi(addToCartBtn)
            }
            //final price
            finalPriceTxt.text = data.finalPrice?.formatWithCommas()
            //click
            addToCartBtn.setOnClickListener {
                if (isAddedToCart == 0) {
                    if (data.quantity!!.toInt() > 0) {
                        //check is any color is available
                        if (isNeededColor) {
                            //check if color is selected
                            if (body.colorId == null) {
                                root.snackbar(getString(R.string.selectTheOneOfColors))
                            } else {
                                cartViewModel.callAddToCartApi(productId, body)
                            }
                        } else {
                            cartViewModel.callAddToCartApi(productId, body)

                        }
                    } else {
                        root.snackbar(getString(R.string.shouldExistsProductInStore))
                    }
                } else {
                    root.snackbar(getString(R.string.alreadyExist))
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadFavoriteData() {
        viewModel.favoriteData.observe(viewLifecycleOwner) {
            binding.detailHeaderLay.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        favLoading.isVisible(false, favImg)
                        it.data?.let { response ->

                            updateFavUi(response.count!!)
                            binding.detailInfoLay.apply {
                                if (response.count == 1) {
                                    rateTxt.text = "${
                                        rateTxt.textString.dropLast(7).toInt().plus(1)
                                    } ${getString(R.string.rate)}"
                                } else {
                                    rateTxt.text = "${
                                        rateTxt.textString.dropLast(7).toInt().minus(1)
                                    } ${getString(R.string.rate)}"

                                }
                            }
                        }

                    }

                    is NetworkStatus.Error -> {
                        favLoading.isVisible(false, favImg)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        favLoading.isVisible(true, favImg)


                    }

                }
            }
        }

    }

    private fun loadAddToCartData() {
        cartViewModel.addToCart.observe(viewLifecycleOwner) {
            binding.detailBottom.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        addToCartBtn.enableLoading(false)
                        it.data?.message?.let { root.snackbar(it) }
                        isAddedToCart = 1
                        updateAddToCartUi(addToCartBtn)
                        launchIoLifeCycle {
                            EventBus.publish(Event.IsUpdateCart)
                        }

                    }

                    is NetworkStatus.Error -> {
                        addToCartBtn.enableLoading(false)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        addToCartBtn.enableLoading(true)


                    }

                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        ::countDownTimer.isInitialized.ifTrue {
            countDownTimer.cancel()
        }
    }
}