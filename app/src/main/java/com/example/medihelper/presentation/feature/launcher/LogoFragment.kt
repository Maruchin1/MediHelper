package com.example.medihelper.presentation.feature.launcher

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.medihelper.R
import com.example.medihelper.domain.usecases.PersonUseCases
import kotlinx.android.synthetic.main.fragment_logo.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class LogoFragment : Fragment() {

    companion object {
        private const val LOGO_TIME = 300L
        private const val ANIM_TIME = 1000L
    }

    private val personUseCases: PersonUseCases by inject()
    private val launcherActivity: LauncherActivity by lazy { requireActivity() as LauncherActivity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_logo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            if (!personUseCases.mainPersonExists()) {
                Handler().postDelayed({
                    launcherActivity.setLightStatusBar()
                    circularHideBackground()
                }, LOGO_TIME)
                Handler().postDelayed({
                    navigateToWelcomeFragment()
                }, LOGO_TIME + ANIM_TIME)
            } else {
                Handler().postDelayed({
                    launcherActivity.startMainActivity()
                }, LOGO_TIME)
            }
        }

    }

    private fun navigateToWelcomeFragment() {
        val extras = FragmentNavigatorExtras(
            card_logo to "card_logo_welcome"
        )
        findNavController().navigate(LogoFragmentDirections.toWelcomeFragment(), extras)
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