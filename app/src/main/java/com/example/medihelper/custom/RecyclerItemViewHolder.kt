package com.example.medihelper.custom

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    val view = binding.root

    fun bind(displayData: Any) {
        binding.apply {
            setVariable(BR.displayData, displayData)
            binding.executePendingBindings()
        }
    }
}