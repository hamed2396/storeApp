package com.example.storeapp.adapters.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.databinding.ItemCategoriesSubBinding
import com.example.storeapp.models.categories.ResponseCategories.ResponseCategoriesItem.SubCategory
import com.example.storeapp.utils.base.BaseDiffUtil
import javax.inject.Inject

class SubCategoryAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>() {

    private var items = emptyList<SubCategory>()
    private lateinit var binding: ItemCategoriesSubBinding
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubCategoryAdapter.ViewHolder {
        binding =
            ItemCategoriesSubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: SubCategoryAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SubCategory) {
            binding.apply {
                itemTitle.text = item.title
                root.setOnClickListener {
                    sendSlug?.let {
                        item.slug?.let { slug ->
                            it(slug)
                        }
                    }
                }


            }


        }
    }

    private var sendSlug: ((String) -> Unit)? = null
    fun getSlug(listener:(String)->Unit){
        sendSlug=listener
    }


    fun setData(newList: List<SubCategory>) {


        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        items = newList

        DiffUtil.calculateDiff(baseDiffUtil).apply {
            dispatchUpdatesTo(this@SubCategoryAdapter)
        }


    }
}