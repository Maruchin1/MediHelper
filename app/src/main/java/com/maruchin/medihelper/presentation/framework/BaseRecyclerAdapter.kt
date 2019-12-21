package com.maruchin.medihelper.presentation.framework

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T>(
    private val layoutResId: Int,
    lifecycleOwner: LifecycleOwner,
    itemsSource: LiveData<List<T>>,
    areItemsTheSameFun: ((oldItem: T, newItem: T) -> Boolean)? = null
) : RecyclerView.Adapter<BaseViewHolder>() {

    protected val itemsList = mutableListOf<T>()
    private var diffCallback: DiffCallback<T>? = null

    init {
        if (areItemsTheSameFun != null) {
            diffCallback = DiffCallback(areItemsTheSameFun)
        }
        itemsSource.observe(lifecycleOwner, Observer { list ->
            updateItemsList(list)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
        return BaseViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun updateItemsList(newList: List<T>?) {
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
                diffCallback!!.setLists(oldList = itemsList, newList = newList)
                val diffResult = DiffUtil.calculateDiff(diffCallback!!)
                itemsList.clear()
                itemsList.addAll(newList)
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }
}