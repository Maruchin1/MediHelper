package com.example.medihelper.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.medihelper.BR
import com.example.medihelper.R

fun <BindingType : ViewDataBinding>Fragment.bind(
    inflater: LayoutInflater,
    layoutResId: Int,
    container: ViewGroup?,
    handler: Fragment = this,
    viewModel: ViewModel? = null
): View {
    val binding: BindingType = DataBindingUtil.inflate(inflater, layoutResId, container, false)
    binding.setVariable(BR.handler, handler)
    binding.lifecycleOwner = this.viewLifecycleOwner
    if (viewModel != null) {
        binding.setVariable(BR.viewModel, viewModel)
    }
    return binding.root
}

fun DialogFragment.setTransparentStatusBar() {
    dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorTransparent)
}