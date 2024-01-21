package com.example.storeapp.adapters.orders


import ProductsAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemOrdersBinding
import com.example.storeapp.models.profile.ResponseOrders
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.DateConvertor.convertDateToFarsi
import com.example.storeapp.utils.extensions.formatWithCommas
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OrdersAdapter @Inject constructor(@ApplicationContext private val context: Context,private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    private var items = emptyList<ResponseOrders.Data>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder(private val binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseOrders.Data) {
            binding.apply {
                itemPrice.text = item.finalPrice.toString().toInt().formatWithCommas()
                //Convert date
                val dateSplit = item.updatedAt!!.split("T")
                val date = dateSplit[0].convertDateToFarsi()
                val hour = "${context.getString(R.string.hour)} ${
                    dateSplit[1].split(".")[0].dropLast(3)
                }"
               calendarTitle.text = "$date | $hour"
                //Products
                item.orderItems?.let { products ->
                    productsList(products, binding)
                }
            }
        }
    }

    private fun productsList(list: List<ResponseOrders.Data.OrderItem>, binding: ItemOrdersBinding) {
        val adapter = ProductsAdapter(baseDiffUtil)
        adapter.setData(list)
        binding.productsList.initRecyclerViewAdapter(adapter)
    }

    fun setData(data: List<ResponseOrders.Data>) {
        baseDiffUtil.setNewList(data)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = data
        result.dispatchUpdatesTo(this)
    }
}