package com.maruchin.medihelper.presentation.framework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.R
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

fun DialogFragment.setLightStatusBar() {
    dialog?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorTransparent)
}

fun Fragment.showShortSnackbar(rootLayout: View, message: String) {
    Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT)
        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
        .show()
}

fun Fragment.showLongSnackbar(rootLayout: View, message: String) {
    Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG)
        .setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
        .show()
}

fun <X, Y> LiveData<X>.map(mapFunction: (input: X) -> Y): LiveData<Y> {
    return Transformations.map(this) { mapFunction.invoke(it) }
}

fun <X, Y> LiveData<X>.switchMap(mapFunction: (input: X) -> LiveData<Y>): LiveData<Y> {
    return Transformations.switchMap(this) { mapFunction.invoke(it) }
}