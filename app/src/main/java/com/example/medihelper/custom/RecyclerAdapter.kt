package com.example.medihelper.custom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerAdapter(
    private val layoutResId: Int,
    private val itemsList: List<Any>
) : RecyclerView.Adapter<RecyclerItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
        return RecyclerItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }
}