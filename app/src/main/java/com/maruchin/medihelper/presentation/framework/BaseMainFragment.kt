package com.maruchin.medihelper.presentation.framework

import android.graphics.Color
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.MainApplication
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.MainActivity

abstract class BaseMainFragment<T : ViewDataBinding>(layoutResId: Int) : BaseFragment<T>(layoutResId) {

    protected val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

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

    protected fun setStatusBarColorLive(colorLive: LiveData<String>) {
        colorLive.observe(viewLifecycleOwner, Observer { color ->
            mainActivity.window?.statusBarColor = Color.parseColor(color)
        })
    }

    protected fun setStatusBarColor(colorId: Int) {
        mainActivity.window?.statusBarColor = ContextCompat.getColor(requireContext(), colorId)
    }

    protected fun setupToolbarNavigation() {
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.setupWithNavController(findNavController())
    }
}