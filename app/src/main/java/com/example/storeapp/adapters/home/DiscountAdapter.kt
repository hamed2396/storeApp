package com.example.storeapp.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.views.color
import com.crazylegend.kotlinextensions.views.deleteLine
import com.crazylegend.kotlinextensions.views.set
import com.crazylegend.kotlinextensions.views.textColor
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.recyclerview.getColor
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemDiscountBinding
import com.example.storeapp.models.home.ResponseDiscount.ResponseDiscountItem
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.formatWithCommas
import javax.inject.Inject

class DiscountAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<DiscountAdapter.ViewHolder>() {

    private var items = emptyList<ResponseDiscountItem>()
    private lateinit var binding: ItemDiscountBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountAdapter.ViewHolder {
        binding = ItemDiscountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: DiscountAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseDiscountItem) {
            binding.apply {
                val image = "${Constants.BASE_URL_DISCOUNT}${item.image}"
                itemImg.loadImage(image)
                itemPriceDiscount.text = item.finalPrice.toString().toInt().formatWithCommas()
                //Discount
                itemPrice.apply {
                    text = item.productPrice.toString().toInt().formatWithCommas()
                    //draw a line on discount
                    deleteLine()
                    textColor = color(R.color.salmon)
                }

                item.quantity?.toInt()?.let {
                    itemProgress.progress = it
                    if (it >= 25) {
                        itemProgress.setIndicatorColor(getColor(R.color.caribbeanGreen))
                    } else {
                        itemProgress.setIndicatorColor(getColor(R.color.salmon))
                    }
                }

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item.id!!)
                    }
                }

            }


        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseDiscountItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}