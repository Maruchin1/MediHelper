package com.example.medihelper.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapter<T>(
    private val layoutResId: Int,
    private val diffCallback: DiffCallback<T>?
) : RecyclerView.Adapter<RecyclerItemViewHolder>() {

    protected val itemsList = mutableListOf<T>()

    open fun updateItemsList(newList: List<T>?) {
        when {
            newList == null -> {
                itemsList.clear()
                notifyDataSetChanged()
            }
            diffCallback == null -> {
                itemsList.clear()
                itemsList.addAll(newList)
                notifyDataSetChanged()
            }
            else -> {
                diffCallback.setLists(oldList = itemsList, newList = newList)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                itemsList.clear()
                itemsList.addAll(newList)
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
        return RecyclerItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}