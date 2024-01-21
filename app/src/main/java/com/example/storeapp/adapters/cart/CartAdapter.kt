package com.example.storeapp.adapters.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.color
import com.crazylegend.kotlinextensions.views.deleteLine
import com.crazylegend.kotlinextensions.views.disable
import com.crazylegend.kotlinextensions.views.enable
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.invisible
import com.crazylegend.kotlinextensions.views.textColor
import com.crazylegend.kotlinextensions.views.visible
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemCartBinding
import com.example.storeapp.models.search.ResponseSearch.Products.*
import com.example.storeapp.models.cart.ResponseCart
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.formatWithCommas
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CartAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
    @ApplicationContext private val context: Context
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private var items = emptyList<ResponseCart.OrderItem>()
    private lateinit var binding: ItemCartBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseCart.OrderItem) {
            binding.apply {
                itemTitle.text = item.productTitle
                val url = "${Constants.BASE_URL_IMAGE}${item.productImage}"
                itemImg.loadImage(url)
                itemCountTxt.text = item.quantity
                colorTxt.text = item.colorTitle

                if (item.productGuarantee.isNotNullOrEmpty()) {
                    guaranteeLay.visible()
                    guaranteeTxt.text = item.productGuarantee
                } else {
                    guaranteeLay.gone()
                }
                item.discountedPrice?.let {
                    itemPriceDiscount.visible()
                    if (it.toInt() > 0) {
                        itemPrice.apply {
                            visible()
                            text = item.finalPrice?.toInt()?.formatWithCommas()
                        }
                        itemPriceDiscount.apply {
                            text = (item.quantity?.toInt()
                                ?.times(item.price!!.toInt()))!!.formatWithCommas()
                            deleteLine()
                            textColor = color(R.color.salmon)
                        }
                    } else {
                        itemPriceDiscount.gone()
                        itemPrice.apply {
                            text = item.price.toString().toInt().formatWithCommas()
                            textColor = color(R.color.darkTurquoise)

                        }
                    }
                }
                if (item.quantity.toString().toInt() == 1) {
                    itemMinusImg.invisible()
                    itemTrashImg.visible()
                } else {
                    itemMinusImg.visible()
                    itemTrashImg.gone()
                }
                if (item.quantity.toString().toInt() == item.productQuantity.toString().toInt()) {
                    itemPlusImg.apply {
                        alpha = .2f
                        disable()
                    }
                } else {
                    itemPlusImg.apply {
                        alpha = 1f
                        enable()
                    }
                }
                itemPlusImg.setOnClickListener {
                    onItemClickListener?.let {
                        it(item.id!!, Constants.INCREMENT)
                    }
                }
                itemMinusImg.setOnClickListener {
                    onItemClickListener?.let {
                        it(item.id!!, Constants.DECREMENT)
                    }
                }
                itemTrashImg.setOnClickListener {
                    onItemClickListener?.let {
                        it(item.id!!, Constants.DELETE)
                    }
                }

            }


        }
    }

    private var onItemClickListener: ((Int, String) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseCart.OrderItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}
