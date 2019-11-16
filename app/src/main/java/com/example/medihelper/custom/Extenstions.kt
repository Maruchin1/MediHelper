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
import com.google.android.material.snackbar.Snackbar

fun <BindingType : ViewDataBinding> Fragment.bind(
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

fun Fragment.showShortSnackbar(rootLayout: View, message: String) {
    Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()
}

fun Fragment.showLongSnackbar(rootLayout: View, message: String) {
    Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
        .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
        .show()
}