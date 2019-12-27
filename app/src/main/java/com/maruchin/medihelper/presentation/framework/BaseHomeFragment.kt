package com.maruchin.medihelper.presentation.framework

import androidx.databinding.ViewDataBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maruchin.medihelper.R

abstract class BaseHomeFragment<T : ViewDataBinding>(layoutResId: Int) : BaseMainFragment<T>(layoutResId) {

    override fun onStart() {
        super.onStart()
        val navController = findNavController()
        val bottomNav: BottomNavigationView = requireView().findViewById(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
    }
}