package com.example.storeapp.adapters.shipping

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.databinding.ItemAddressesDialogBinding
import com.example.storeapp.databinding.ItemShippingProductBinding
import com.example.storeapp.models.shipping.ResponseShipment
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.loadImageWithGlide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShippingAddressesAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
) :
    RecyclerView.Adapter<ShippingAddressesAdapter.ViewHolder>() {

    private var items = emptyList<ResponseShipment.Addresse>()
    private lateinit var binding: ItemAddressesDialogBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingAddressesAdapter.ViewHolder {
        binding =
            ItemAddressesDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ShippingAddressesAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item:ResponseShipment.Addresse) {
            binding.apply {
                itemNameTxt.text="${item.receiverFirstname} ${item.receiverLastname}"
                itemAddressTxt.text=item.postalAddress
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }


        }
    }

    private var onItemClickListener: ((ResponseShipment.Addresse) -> Unit)? = null
    fun setOnItemClickListener(listener: (ResponseShipment.Addresse) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseShipment.Addresse>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}