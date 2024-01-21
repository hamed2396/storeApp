package com.example.storeapp.adapters.addresses

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemAddressesBinding
import com.example.storeapp.models.address.ResponseAddresses.ResponseAddressesItem
import com.example.storeapp.utils.base.BaseDiffUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AddressesAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
    @ApplicationContext private val context: Context
) :
    RecyclerView.Adapter<AddressesAdapter.ViewHolder>() {

    private var items = emptyList<ResponseAddressesItem>()
    private lateinit var binding: ItemAddressesBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesAdapter.ViewHolder {
        binding = ItemAddressesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: AddressesAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseAddressesItem) {
            binding.apply {
                nameTxt.text = "${item.receiverFirstname} ${item.receiverLastname}"
                provinceTxt.text = item.province?.title
                cityTxt.text = item.city?.title
                locationTxt.text =
                    "${item.postalAddress} - ${context.getString(R.string.plateNumber)} ${item.plateNumber} ${
                        context.getString(R.string.floor)
                    } ${item.floor}"
                postalTxt.text = item.postalCode
                phoneTxt.text = item.receiverCellphone
                root.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }

            }


        }
    }

    private var onItemClickListener: ((ResponseAddressesItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (ResponseAddressesItem) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseAddressesItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}