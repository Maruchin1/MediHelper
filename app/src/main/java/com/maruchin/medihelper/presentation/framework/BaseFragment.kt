package com.maruchin.medihelper.presentation.framework

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.MainApplication
import com.maruchin.medihelper.R

abstract class BaseFragment<T : ViewDataBinding>(private val layoutResId: Int) : Fragment() {

    protected val mainApplication: MainApplication
        get() = requireContext().applicationContext as MainApplication
    protected var bindingViewModel: ViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: T = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.apply {
            setVariable(BR.handler, this@BaseFragment)
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, bindingViewModel)
        }.root
    }

    protected fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}