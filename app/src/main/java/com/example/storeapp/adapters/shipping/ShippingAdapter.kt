package com.example.storeapp.adapters.shipping

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.databinding.ItemShippingProductBinding
import com.example.storeapp.models.shipping.ResponseShipment
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.loadImageWithGlide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShippingAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
) :
    RecyclerView.Adapter<ShippingAdapter.ViewHolder>() {

    private var items = emptyList<ResponseShipment.Order.OrderItem>()
    private lateinit var binding: ItemShippingProductBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingAdapter.ViewHolder {
        binding =
            ItemShippingProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ShippingAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseShipment.Order.OrderItem) {
            binding.apply {
                val image = "${Constants.BASE_URL_IMAGE}${item.productImage}"
                itemImg.loadImageWithGlide(image)
                itemTitle.text=item.productTitle

            }


        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseShipment.Order.OrderItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}