package com.example.storeapp.adapters.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.storeapp.databinding.ItemCategoriesBinding
import com.example.storeapp.models.categories.ResponseCategories.ResponseCategoriesItem
import com.example.storeapp.models.search.ResponseSearch.Products.*
import com.example.storeapp.utils.base.BaseDiffUtil
import javax.inject.Inject

class CategoriesAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var items = emptyList<ResponseCategoriesItem>()
    private lateinit var binding: ItemCategoriesBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.ViewHolder {
        binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseCategoriesItem) {
            binding.apply {

                itemTitle.text = item.title
                if (item.subCategories!!.isNotEmpty()) {
                    mainLay.visible()
                    subCategoriesAdapter(item.subCategories)
                } else {
                    mainLay.gone()
                }


            }


        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseCategoriesItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }

    private fun subCategoriesAdapter(list: List<ResponseCategoriesItem.SubCategory>) {

        val subCategories = SubCategoryAdapter(baseDiffUtil)
        subCategories.getSlug { slug ->
            onItemClickListener?.let {
                it(slug)
            }
        }
        subCategories.setData(list)
        binding.itemSubCatsList.initRecyclerViewAdapter(
            subCategories,
            LinearLayoutManager.HORIZONTAL,
            reverseLayout = true
        )
    }
}