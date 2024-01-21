package com.example.storeapp.ui.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.crazylegend.kotlinextensions.collections.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentDetailChartBinding
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseFragment
import com.example.storeapp.utils.extensions.isVisible
import com.example.storeapp.utils.extensions.setupMyChart
import com.example.storeapp.utils.network.NetworkStatus
import com.example.storeapp.viewModels.DetailViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailChartFragment : BaseFragment(R.layout.fragment_detail_chart) {

    private val binding by viewBinding(FragmentDetailChartBinding::bind)
    private val viewModel by viewModels<DetailViewModel>()
    private val daysList = arrayListOf<String>()
    private val daysListForTooltip = arrayListOf<String>()
    private val pricesList = ArrayList<Entry>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.callGetChartApi(Constants.PRODUCT_ID)
        loadChartData()


    }

    private fun loadChartData() {
        viewModel.getChartData.observe(viewLifecycleOwner) {
            binding.apply {
                when (it) {
                    is NetworkStatus.Data -> {
                        featuresLoading.isVisible(false, pricesChart)
                        it.data?.let { data ->
                            daysList.clear()
                            daysListForTooltip.clear()
                            pricesList.clear()

                            if (data.isNotEmpty()) {
                                for (i in data.indices) {
                                    daysListForTooltip.add(data[i].day!!)
                                    daysList.add(data[i].day!!.drop(5))
                                    if (data[i].price!! > 0)
                                        pricesList.add(Entry(i.toFloat(), data[i].price!!.toFloat()))
                                }
                                //Init chart
                                lifecycleScope.launch {
                                    delay(100)
                                    if (pricesList.isNotEmpty()) {
                                        pricesChart.setupMyChart(
                                            DaysFormatter(daysList), pricesList, daysList.size,
                                            daysListForTooltip
                                        )
                                    }
                                }
                            }
                        }

                    }

                    is NetworkStatus.Error -> {
                        featuresLoading.isVisible(false, pricesChart)

                        root.snackbar(it.error!!)
                    }

                    is NetworkStatus.Loading -> {
                        featuresLoading.isVisible(true, pricesChart)


                    }

                }
            }
        }

    }

    inner class DaysFormatter(val daysList: ArrayList<String>) : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
            val index = value.toInt()
            return if (index < daysList.size) daysList[index] else null
        }

    }


}