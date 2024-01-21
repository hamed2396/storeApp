package com.example.storeapp.adapters.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.databinding.ItemMyCommentsBinding
import com.example.storeapp.models.comment.ResponseComments.Data
import com.example.storeapp.utils.Constants
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.Extension.loadImage
import javax.inject.Inject

class CommentsAdapter @Inject constructor(private val baseDiffUtil: BaseDiffUtil<Any>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var items = emptyList<Data>()
    private lateinit var binding: ItemMyCommentsBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.ViewHolder {
        binding = ItemMyCommentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Data) {
            binding.apply {
                item.product?.let {
                    itemCommentTxt.text = it.title
                    val image = "${Constants.BASE_URL_IMAGE}${it.image}"
                    itemImg.loadImage(image)
                }
                item.rate?.let { itemRating.rating = it.toFloat() }
                itemCommentTxt.text = item.comment
                itemTrashImg.setOnClickListener {
                    onItemClickListener?.let { item.id?.let { id -> it(id) } }
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