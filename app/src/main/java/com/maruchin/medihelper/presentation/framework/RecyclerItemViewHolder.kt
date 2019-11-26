package com.maruchin.medihelper.presentation.framework

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    val view = binding.root

    fun bind(displayData: Any, handler: Any? = null, position: Int? = null) {
        binding.apply {
            setVariable(BR.displayData, displayData)
            handler?.let { setVariable(BR.handler, it) }
            position?.let { setVariable(BR.position, it) }
            binding.executePendingBindings()
        }
    }
}