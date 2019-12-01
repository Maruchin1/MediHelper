package com.maruchin.medihelper.presentation.framework

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maruchin.medihelper.R

abstract class BaseMainFragment<T : ViewDataBinding>(layoutResId: Int) : BaseFragment<T>(layoutResId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val bottomNav: BottomNavigationView = view.findViewById(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
    }
}