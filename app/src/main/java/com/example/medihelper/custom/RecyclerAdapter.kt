package com.example.medihelper.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapter<T>(private val layoutResId: Int) : RecyclerView.Adapter<RecyclerItemViewHolder>() {

    val itemsArrayList: ArrayList<T> = ArrayList()

    fun setItemsList(list: List<T>?) {
        itemsArrayList.clear()
        if (list != null) {
            itemsArrayList.addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
        return RecyclerItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsArrayList.size
    }
}