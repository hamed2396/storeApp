package com.example.storeapp.utils.base

import androidx.recyclerview.widget.DiffUtil
import javax.inject.Inject

class BaseDiffUtil<T>@Inject constructor() : DiffUtil.Callback() {
    private lateinit var newList: List<T>
    private lateinit var oldList: List<T>

    fun setNewList(list: List<T>){
        newList=list
    }
    fun setOldList(list: List<T>){
        oldList=list
    }
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }
}