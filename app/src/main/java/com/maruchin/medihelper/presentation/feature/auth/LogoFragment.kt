package com.maruchin.medihelper.presentation.feature.auth

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentLogoBinding
import com.maruchin.medihelper.presentation.LauncherActivity
import com.maruchin.medihelper.presentation.framework.BaseLauncherFragment
import com.maruchin.medihelper.presentation.framework.BaseMainFragment
import kotlinx.android.synthetic.main.fragment_logo.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogoFragment : BaseLauncherFragment<FragmentLogoBinding>(R.layout.fragment_logo) {

    companion object {
        private const val LOGO_TIME = 300L
        private const val ANIM_TIME = 1000L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(LOGO_TIME)
            super.setLightStatusBar()
            circularHideBackground()
        }
        lifecycleScope.launch {
            delay(LOGO_TIME + ANIM_TIME)
            navigateToWelcomeFragment()
        }
    }

    private fun navigateToWelcomeFragment() {
        val extras = FragmentNavigatorExtras(
            card_logo to "card_logo_login"
        )
        findNavController().navigate(LogoFragmentDirections.toLoginFragment(), extras)
    }

    private fun circularHideBackground() {
        val cx = lay_background.width / 2
        val cy = lay_background.height / 2

        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(lay_background, cx, cy, initialRadius, 0f)
        anim.interpolator = FastOutSlowInInterpolator()
        anim.duration = ANIM_TIME
        anim.start()
    }
}