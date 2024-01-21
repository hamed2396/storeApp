package com.example.storeapp.adapters.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.string.asColor
import com.crazylegend.kotlinextensions.views.backgroundColor
import com.example.storeapp.databinding.ItemColorsListBinding
import com.example.storeapp.models.search.ResponseSearch.Products.Data.Color
import com.example.storeapp.utils.base.BaseDiffUtil
import javax.inject.Inject

class ColorsAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<ColorsAdapter.ViewHolder>() {

    private var items = emptyList<Color>()
    private lateinit var binding: ItemColorsListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsAdapter.ViewHolder {
        binding = ItemColorsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ColorsAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Color) {
            binding.apply {
                item.hexCode?.asColor?.let {
                    itemsColor.backgroundColor = it
                }


            }


        }
    }


    fun setData(newList: List<Color>) {

        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        items = newList

        DiffUtil.calculateDiff(baseDiffUtil).apply {
            dispatchUpdatesTo(this@ColorsAdapter)
        }


    }
}