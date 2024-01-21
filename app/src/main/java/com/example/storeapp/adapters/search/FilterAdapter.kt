package com.example.storeapp.adapters.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.storeapp.databinding.ItemFilterBinding
import com.example.storeapp.models.search.ResponseSearch.Products.*
import com.example.storeapp.models.search.SearchFilterModel
import com.example.storeapp.utils.base.BaseDiffUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilterAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
) :
    RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    private var items = emptyList<SearchFilterModel>()
    private lateinit var binding: ItemFilterBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: SearchFilterModel) {
            binding.apply {

                itemTitle.text = item.fName
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item.eName)

                    }
                }

            }
        }

    }
    fun setData(newList: List<SearchFilterModel>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this@FilterAdapter)

    }
    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}