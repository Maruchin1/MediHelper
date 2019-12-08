package com.maruchin.medihelper.presentation.framework

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.MainApplication
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.MainActivity

abstract class BaseFragment<T : ViewDataBinding>(private val layoutResId: Int) : Fragment() {

    protected val mainActivity: MainActivity
        get() = requireActivity() as MainActivity
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

    protected fun setLightStatusBar(light: Boolean) {
        mainActivity.window?.statusBarColor = if (light) {
            ContextCompat.getColor(requireContext(), R.color.colorBackground)
        } else {
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        }

        mainActivity.window?.decorView?.systemUiVisibility =
            if (light && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                0
            }
    }

    protected fun setupToolbarNavigation() {
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setupWithNavController(findNavController())
    }
}