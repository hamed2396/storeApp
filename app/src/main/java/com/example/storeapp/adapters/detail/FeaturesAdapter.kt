package com.example.storeapp.adapters.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.recyclerview.getColor
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemFeaturesBinding
import com.example.storeapp.models.details.ResponseFeatures.ResponseFeturesItem
import com.example.storeapp.utils.base.BaseDiffUtil
import javax.inject.Inject

class FeaturesAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
) :
    RecyclerView.Adapter<FeaturesAdapter.ViewHolder>() {

    private var items = emptyList<ResponseFeturesItem>()
    private lateinit var binding: ItemFeaturesBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesAdapter.ViewHolder {
        binding =
            ItemFeaturesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: FeaturesAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseFeturesItem) {
            binding.apply {
                titleTxt.text = item.featureTitle
                infoTxt.text = item.featureItemTitle
                //background
                if (bindingAdapterPosition.mod(2) == 0) root.setBackgroundColor(getColor(R.color.white)) else root.setBackgroundColor(
                    getColor(R.color.snow)
                )
            }


        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseFeturesItem>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}