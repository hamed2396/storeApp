package com.example.storeapp.ui.home

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import coil.load
import com.crazylegend.kotlinextensions.collections.isListAndNotNullOrEmpty
import com.crazylegend.kotlinextensions.ifNotNull
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.snackbarLong
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.kotlinextensions.whether
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.crazylegend.recyclerview.withPagerSnapHelper
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.adapters.home.BannerAdapter
import com.example.storeapp.adapters.home.DiscountAdapter
import com.example.storeapp.adapters.home.ProductAdapter
import com.example.storeapp.databinding.DialogCheckVpnBinding
import com.example.storeapp.databinding.FragmentHomeBinding
import com.example.storeapp.models.home.ResponseBanners.ResponseBannersItem
import com.example.storeapp.models.home.ResponseDiscount.ResponseDiscountItem
import com.example.storeapp.models.home.ResponseProducts
import com.example.storeapp.models.home.ResponseProducts.SubCategory.Products.Data
import com.example.storeapp.ui.categories.CategoryFragmentDirections
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.ProductsCategories
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.Extension.launchIoLifeCycle
import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.transparentCorners
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.HomeViewModel
import com.example.storeapp.viewModels.ProfileViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val profileViewModel by activityViewModels<ProfileViewModel>()
    private val snapHelper by lazy { PagerSnapHelper() }
    private lateinit var discountCountDownTimer: CountDownTimer


    @Inject
    lateinit var bannerAdapter: BannerAdapter

    @Inject
    lateinit var discountAdapter: DiscountAdapter

    @Inject
    lateinit var mobileProductAdapter: ProductAdapter

    @Inject
    lateinit var shoesProductAdapter: ProductAdapter

    @Inject
    lateinit var stationaryProductAdapter: ProductAdapter

    @Inject
    lateinit var laptopProductAdapter: ProductAdapter
    private val viewModel by activityViewModels<HomeViewModel>()

    @Inject
    lateinit var checkVpn: Flow<Boolean>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            restoreScrollPosition()
            avatarImg.setOnClickListener { findNavController().navigate(R.id.actionHomeToProfile) }
            searchImg.setOnClickListener { findNavController().navigate(R.id.actionToSearch) }
        }
        loadProfileData()
        loadBannerData()
        loadDiscountData()
        loadProductData()
        checkHasVon()


    }

    private fun checkHasVon() {
        launchIoLifeCycle {
            checkVpn.collect {
                if (it) showVpnDialog()
            }
        }
    }

    private fun showVpnDialog() {
        binding.apply {
            val dialogBinding = DialogCheckVpnBinding.inflate(layoutInflater)
            Dialog(requireContext()).apply {
                transparentCorners()
                setContentView(dialogBinding.root)
                dialogBinding.yesBtn.setOnClickListener { dismiss() }
                show()
            }


        }
    }

    private fun loadProfileData() {
        profileViewModel.profileData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        avatarLoading.gone()
                        it.data?.let { response ->

                            (response.avatar != null).whether({
                                avatarImg.loadImage(response.avatar!!)
                                avatarBadgeImg.gone()
                            }, {
                                avatarBadgeImg.visible()
                                avatarImg.load(R.drawable.placeholder_user)

                            })
                        }
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

    private fun loadDiscountData() {
        viewModel.discountData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        discountList.hideShimmer()
                        it.data.isListAndNotNullOrEmpty({
                            //if null or empty
                            discountTimerContainer.gone()

                        }, {
                            //if not null or empty
                            setUpDiscountList(it.data!!)



                            discountTimer(it.data[0].endTime!!)
                            discountCountDownTimer.start()


                        })

                    }

                    is NetworkStatus.Error -> {
                        discountList.hideShimmer()
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        discountList.showShimmer()

                    }

                }
            }
        }

    }

    private fun loadBannerData() {
        viewModel.bannerData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        bannerLoading.isVisible(false, bannerList)
                        it.data.isListAndNotNullOrEmpty({
                            //if null or empty

                        }, {
                            //if not null or empty
                            setUpBannersList(it.data!!)


                        })

                    }

                    is NetworkStatus.Error -> {
                        bannerLoading.isVisible(false, bannerList)
                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        bannerLoading.isVisible(true, bannerList)

                    }

                }
            }
        }

    }

    private fun discountTimer(dateString: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())

        try {
            val date = formatter.parse(dateString)
            val currentTime = System.currentTimeMillis()

            // Calculate the time difference in milliseconds
            date?.time?.let {

                val timeDifference = it - currentTime

                // Handle the time difference as needed
                discountCountDownTimer = object : CountDownTimer(timeDifference, 1000) {
                    override fun onTick(millis: Long) {
                        var timer = millis

                        val day: Long = TimeUnit.MILLISECONDS.toDays(timer)
                        timer -= TimeUnit.DAYS.toMillis(day)

                        val hours: Long = TimeUnit.MILLISECONDS.toHours(timer)
                        timer -= TimeUnit.HOURS.toMillis(hours)

                        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timer)
                        timer -= TimeUnit.MINUTES.toMillis(minutes)

                        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(timer)

                        binding.timerLay.apply {
                            dayTxt.text = day.toString()
                            hourTxt.text = hours.toString()
                            minuteTxt.text = minutes.toString()
                            secondTxt.text = seconds.toString()
                        }
                    }

                    override fun onFinish() {
                        binding.discountTimerContainer.gone()
                    }

                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            // Handle parsing error
        }

    }

    private fun setUpDiscountList(list: List<ResponseDiscountItem>) {
        binding.apply {
            discountAdapter.setData(list)
            discountList.initRecyclerViewAdapter(
                discountAdapter,
                LinearLayoutManager.HORIZONTAL,
                true,
                reverseLayout = true
            )
            discountAdapter.setOnItemClickListener {
                HomeFragmentDirections.actionToDetails(it)
                    .apply { findNavController().navigate(this) }
            }
        }
    }

    private fun setUpBannersList(list: List<ResponseBannersItem>) {
        binding.apply {
            bannerAdapter.setData(list)

            bannerList.apply {
                adapter = bannerAdapter
                set3DItem(true)
                setAlpha(true)
                setInfinite(false)
                withPagerSnapHelper()
                bannerIndicator.attachToRecyclerView(this, snapHelper)


            }

            bannerAdapter.setOnItemClickListener { sendData, type ->
                type.logError()
                if (type == Constants.PRODUCT) {
                    HomeFragmentDirections.actionToDetails(sendData.toInt()).apply {
                        findNavController().navigate(this)
                    }
                } else {
                    HomeFragmentDirections.actionNavCatToNavCatProduct(sendData).apply {
                        findNavController().navigate(this)
                    }

                }
            }
        }
    }

    private fun loadProductData() {
        binding.apply {
            mobileLay.parent.ifNotNull {
                val mobileInflate = mobileLay.inflate()
                viewModel.getProductsData(ProductsCategories.MOBILE).observe(viewLifecycleOwner) {
                    handleProductRequest(
                        it,
                        mobileInflate.findViewById(R.id.mobileProductsList),
                        mobileProductAdapter
                    )
                }
                true
            }
            mobileLay.parent.ifNotNull {
                val mobileInflate = mobileLay.inflate()
                viewModel.getProductsData(ProductsCategories.MOBILE).observe(viewLifecycleOwner) {
                    handleProductRequest(
                        it,
                        mobileInflate.findViewById(R.id.mobileProductsList),
                        mobileProductAdapter
                    )
                }
                true
            }
            shoesLay.parent.ifNotNull {
                val shoesInflate = shoesLay.inflate()
                viewModel.getProductsData(ProductsCategories.SHOES).observe(viewLifecycleOwner) {
                    handleProductRequest(
                        it,
                        shoesInflate.findViewById(R.id.menShoesProductsList),
                        shoesProductAdapter
                    )
                }
                true
            }
            stationeryLay.parent.ifNotNull {
                val stationeryInflate = stationeryLay.inflate()
                viewModel.getProductsData(ProductsCategories.STATIONARY)
                    .observe(viewLifecycleOwner) {
                        handleProductRequest(
                            it,
                            stationeryInflate.findViewById(R.id.stationeryProductsList),
                            stationaryProductAdapter
                        )
                    }
                true
            }
            laptopLay.parent.ifNotNull {
                val laptopInflate = laptopLay.inflate()
                viewModel.getProductsData(ProductsCategories.LAPTOP).observe(viewLifecycleOwner) {
                    handleProductRequest(
                        it,
                        laptopInflate.findViewById(R.id.laptopProductsList),
                        laptopProductAdapter
                    )
                }
                true
            }

        }

    }

    private fun handleProductRequest(
        request: NetworkStatus<ResponseProducts>,
        recyclerView: ShimmerRecyclerView,
        adapter: ProductAdapter
    ) {
        when (request) {
            is NetworkStatus.Data -> {
                recyclerView.hideShimmer()
                request.data?.subCategory?.products?.data?.let {
                    initProductsRecyclers(it, recyclerView, adapter)


                }
            }

            is NetworkStatus.Error -> {
                recyclerView.hideShimmer()
                binding.root.snackbarLong(request.error!!)
            }

            is NetworkStatus.Loading -> {
                recyclerView.showShimmer()
            }
        }

    }

    private fun initProductsRecyclers(
        data: List<Data>,
        recyclerView: ShimmerRecyclerView,
        adapter: ProductAdapter
    ) {
        adapter.setData(data)
        recyclerView.initRecyclerViewAdapter(
            adapter,
            LinearLayoutManager.HORIZONTAL,
            true,
            reverseLayout = true
        )
        adapter.setOnItemClickListener {
         HomeFragmentDirections.actionToDetails(it).apply { findNavController().navigate(this) }

        }

    }

    override fun onStop() {
        super.onStop()
        if (::discountCountDownTimer.isInitialized) discountCountDownTimer.cancel()
    }


    override fun onPause() {
        super.onPause()
        viewModel.lastStateOfScroll = binding.scrollLay.onSaveInstanceState()

    }


    private fun restoreScrollPosition() {
        viewModel.lastStateOfScroll?.let {

            binding.scrollLay.onRestoreInstanceState(it)


        }
    }


}