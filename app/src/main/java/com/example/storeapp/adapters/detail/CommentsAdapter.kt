package com.example.storeapp.adapters.detail

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storeapp.R
import com.example.storeapp.databinding.ItemCommentBinding
import com.example.storeapp.models.details.ResponseProductComments
import com.example.storeapp.utils.base.BaseDiffUtil
import com.example.storeapp.utils.extensions.DateConvertor.convertDateToFarsi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CommentsAdapter @Inject constructor(
    private val baseDiffUtil: BaseDiffUtil<Any>,
    @ApplicationContext private val context: Context
) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var items = emptyList<ResponseProductComments.Data>()
    private lateinit var binding: ItemCommentBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsAdapter.ViewHolder {
        binding =
            ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.toLong()

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ResponseProductComments.Data) {
            binding.apply {
                itemRating.rating = item.rate?.toFloat()!!
                item.user?.let {
                    if (it.firstname != null) {
                        itemNameTxt.text = "${item.user.firstname} ${item.user.lastname}"
                    }else{
                        itemNameTxt.text =context.getString(R.string.withoutName)
                    }
                }

                itemCommentTxt.text = item.comment
                val date = item.createdAt!!.split("T")[0].convertDateToFarsi()
                val hour = "${context.getString(R.string.hour)} ${
                    item.createdAt.split("T")[1].split(".")[0].dropLast(3)
                }"
                itemDateTxt.text = "$date - $hour"
            }


        }
    }

    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(newList: List<ResponseProductComments.Data>) {
        baseDiffUtil.setNewList(newList)
        baseDiffUtil.setOldList(items)
        val result = DiffUtil.calculateDiff(baseDiffUtil)
        items = newList
        result.dispatchUpdatesTo(this)

    }
}