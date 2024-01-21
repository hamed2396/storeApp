package com.example.storeapp.adapters.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.root.logError
import com.example.storeapp.databinding.ItemBannersBinding
import com.example.storeapp.models.home.ResponseBanners.ResponseBannersItem
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import javax.inject.Inject

class BannerAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    private var items = emptyList<ResponseBannersItem>()
    private lateinit var binding: ItemBannersBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerAdapter.ViewHolder {
        binding = ItemBannersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: BannerAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ResponseBannersItem) {
            binding.apply {
                val image = "${Constants.BASE_URL_STORAGE}${item.image}"
                itemImg.loadImage(image)
                root.setOnClickListener {
                    val sendData = if (item.link == Constants.PRODUCT) {
                        item.linkId
                    } else {
                        item.urlLink?.slug
                    }
                    onItemClickListener?.let {
                        it(sendData!!,item.link!!)
                    }
                }

            }


        }
    }

    private var onItemClickListener: ((String, String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseBannersItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}