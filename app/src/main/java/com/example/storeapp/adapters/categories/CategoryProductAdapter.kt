package com.example.storeapp.adapters.categories

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.color
import com.crazylegend.kotlinextensions.views.deleteLine
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.textColor
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.storeapp.R
import com.example.storeapp.adapters.search.ColorsAdapter
import com.example.storeapp.databinding.ItemSearchListBinding
import com.example.storeapp.models.home.ResponseProducts
import com.example.storeapp.models.search.ResponseSearch.Products.*
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.formatWithCommas
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoryProductAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
    @ApplicationContext private val context: Context
) :
    RecyclerView.Adapter<CategoryProductAdapter.ViewHolder>() {

    private var items = emptyList<ResponseProducts.SubCategory.Products.Data>()
    private lateinit var binding: ItemSearchListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductAdapter.ViewHolder {
        binding = ItemSearchListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CategoryProductAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseProducts.SubCategory.Products.Data) {
            binding.apply {
                val image = "${Constants.BASE_URL_DISCOUNT}${item.image}"
                itemImg.loadImage(image)
                itemTitle.text = item.title

                itemQuantity.text =
                    "${context.getString(R.string.quantity)} ${item.quantity} ${context.getString(R.string.item)}"

                item.discountedPrice?.let {
                    if ((it > 0)) {
                        itemDiscount.visible()
                        itemPriceDiscount.visible()
                        itemDiscount.apply {
                            text = item.discountedPrice.toString().toInt().formatWithCommas()
                            textColor = color(R.color.whiteSmoke)
                        }
                        itemPrice.apply {
                            textColor = color(R.color.salmon)
                            text = item.productPrice.toString().toInt().formatWithCommas()
                            visible()
                            deleteLine()
                        }
                        itemPriceDiscount.apply {
                            visible()
                            text = item.finalPrice.toString().toInt().formatWithCommas()

                        }


                    } else {
                        itemDiscount.gone()
                        itemPriceDiscount.gone()
                        itemPrice.apply {
                            text = item.productPrice.toString().toInt().formatWithCommas()
                            textColor = color(R.color.darkTurquoise)
                        }
                    }
                }

                //colors
                item.colors?.let {
                    setColorsAdapter(it) }
                root.setOnClickListener { }

            }


        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseProducts.SubCategory.Products.Data>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }

    private fun setColorsAdapter(list: List<Data.Color>) {
        val colorAdapter = ColorsAdapter(baseDiffUtil)


        colorAdapter.setData(list)
        binding.itemsColors.initRecyclerViewAdapter(colorAdapter, LinearLayoutManager.HORIZONTAL, reverseLayout = true)
    }
}