package com.maruchin.medihelper.presentation.feature.authentication

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentLogoBinding
import com.maruchin.medihelper.domain.usecases.user.IsUserSignedInUseCase
import com.maruchin.medihelper.presentation.framework.BaseLauncherFragment
import kotlinx.android.synthetic.main.fragment_logo.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LogoFragment : BaseLauncherFragment<FragmentLogoBinding>(R.layout.fragment_logo) {

    companion object {
        private const val LOGO_TIME = 300L
        private const val ANIM_TIME = 1000L
    }

    private val isUserSignedInUseCase: IsUserSignedInUseCase by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val signedIn = isUserSignedInUseCase.execute()

            delay(LOGO_TIME)
            if (signedIn) {
                super.launcherActivity.startMainActivity()
            } else {
                super.setLightStatusBar()
                circularHideBackground()

                delay(ANIM_TIME)
                navigateToLoginFragment()
            }
        }
    }

    private fun navigateToLoginFragment() {
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