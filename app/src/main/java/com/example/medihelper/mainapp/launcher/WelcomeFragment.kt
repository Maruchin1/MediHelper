package com.example.medihelper.mainapp.launcher

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentWelcomeBinding
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    private val directions by lazy { WelcomeFragmentDirections }

    fun onClickNavToMainPersonFragment() = findNavController().navigate(
        directions.toMainPersonFragment(),
        getSharedLogoExtras()
    )

    fun onClickNavToLoginFragment() = findNavController().navigate(
        directions.toLoginFragment(),
        getSharedLogoExtras()
    )

    fun onClickNavToPatronConnectFragment() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(LauncherTransitions()) {
            sharedElementEnterTransition = getWelcomeEnterShared(requireContext())
            enterTransition = getWelcomeEnter()
            exitTransition = getWelcomeExit()
            reenterTransition = getWelcomeReenter()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentWelcomeBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_welcome
        )
    }

    private fun getSharedLogoExtras() = FragmentNavigatorExtras(
        lay_logo to "lay_logo_option"
    )
}