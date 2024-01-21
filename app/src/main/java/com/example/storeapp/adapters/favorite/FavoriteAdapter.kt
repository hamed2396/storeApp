package com.example.storeapp.adapters.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemMyFavoritesBinding
import com.example.storeapp.models.favorite.ResponseFavorite
import com.example.storeapp.models.favorite.ResponseFavorite.Data
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import com.example.storeapp.utils.extensions.formatWithCommas
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FavoriteAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>,@ApplicationContext private val  context: Context) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var items = emptyList<Data>()
    private lateinit var binding: ItemMyFavoritesBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        binding = ItemMyFavoritesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Data) {
            binding.apply {
            item.likeable?.let {
                itemTitle.text = it.title
                quantityTxt.text="${it.quantity}${context.getString(R.string.item)}"
                priceTxt.text="${it.finalPrice?.formatWithCommas()}"
                val image = "${Constants.BASE_URL_IMAGE}${it.image}"
                itemImg.loadImage(image)
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