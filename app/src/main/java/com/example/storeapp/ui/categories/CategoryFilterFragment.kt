package com.example.storeapp.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.views.isNotEmpty
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.viewbinding.viewBinding
import com.example.storeapp.R
import com.example.storeapp.databinding.FragmentCategoriesFilterBinding
import com.example.storeapp.models.search.SearchFilterModel
import com.example.storeapp.utils.extensions.formatWithCommas
import com.example.storeapp.viewModels.CategoryProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFilterFragment : BottomSheetDialogFragment(R.layout.fragment_categories_filter) {

    private val binding by viewBinding(FragmentCategoriesFilterBinding::bind)

    private var minPrice: String? = null
    private var maxPrice: String? = null
    private var sort: String? = null
    private var isAvailable: Boolean? = null
    private var search: String? = null
    private val viewModel by activityViewModels<CategoryProductViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            closeImg.setOnClickListener { dismiss() }
            initPriceRange()
            getFilterList()
            viewModel.getFilterList()
            submitBtn.setOnClickListener {
                if (searchEdt.isNotEmpty()) {
                    search = searchEdt.textString
                }
                isAvailable = availableCheck.isChecked
                viewModel.sendFilterCategoryItem( sort, minPrice, maxPrice,isAvailable,search)

                dismiss()
            }


        }

    }

    private fun getFilterList() {
        viewModel.getFilterList()
        viewModel.filterData.observe(viewLifecycleOwner) {
            setUpChip(it)
        }
    }


    override fun getTheme() = R.style.RemoveDialogBackground
    private fun initPriceRange() {
        val formatter = LabelFormatter { value ->
            value.toInt().formatWithCommas()
        }
        binding.priceRange.apply {
            setValues(7000000f, 21000000f)
            setLabelFormatter(formatter)
            addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {

                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    val values = slider.values
                    minPrice = values[0].toInt().toString()
                    maxPrice = values[1].toInt().toString()
                }
            })
        }
    }

    private fun setUpChip(list: MutableList<SearchFilterModel>) {
        var tempList = mutableListOf<SearchFilterModel>()
        tempList.clear()
        tempList = list
        tempList.forEach {
            val chip = Chip(requireContext()).apply {
                val drawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.FilterChipsBackground
                )
                setChipDrawable(drawable)
                setTextColor(compatColor(R.color.white))
                id = it.id
                text = it.fName
                setTextAppearance(R.style.FilterChipsText)


            }
            binding.sortChipGroup.apply {
                addView(chip)
                setOnCheckedStateChangeListener { group, _ ->
                    sort = tempList[group.checkedChipId - 1].eName
                }
            }

        }

    }


}