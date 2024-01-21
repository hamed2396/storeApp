package com.example.storeapp.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.color
import com.crazylegend.kotlinextensions.views.deleteLine
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.textColor
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.getColor
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemProductsBinding
import com.example.storeapp.models.home.ResponseProducts.SubCategory.Products.Data
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.formatWithCommas
import javax.inject.Inject

class ProductAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var items = emptyList<Data>()
    private lateinit var binding: ItemProductsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        binding = ItemProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data) {
            binding.apply {
                val image = "${Constants.BASE_URL_DISCOUNT}${item.image}"
                itemImg.loadImage(image)
                itemTitle.text = item.title

                item.quantity?.toInt()?.let {
                    itemProgress.progress = it
                    if (it >= 25) {
                        itemProgress.setIndicatorColor(getColor(R.color.caribbeanGreen))
                    } else {
                        itemProgress.setIndicatorColor(getColor(R.color.salmon))
                    }
                }
                item.discountedPrice?.let {
                    if ((it > 0)) {
                        itemDiscount.visible()
                        itemPriceDiscount.visible()
                        itemDiscount.apply {
                            text = item.discountedPrice.toString().toInt().formatWithCommas()
                            textColor = color(R.color.whiteSmoke)
                        }
                        itemPrice.apply {
                            textColor=color(R.color.salmon)
                            text=item.productPrice.toString().toInt().formatWithCommas()
                            visible()
                            deleteLine()
                        }
                        itemPriceDiscount.apply {
                            visible()
                            text=item.finalPrice.toString().toInt().formatWithCommas()

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
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }
                }

            }


        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener

    }

    fun setData(newList: List<Data>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}