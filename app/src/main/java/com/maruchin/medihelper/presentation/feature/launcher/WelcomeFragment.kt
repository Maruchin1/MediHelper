package com.maruchin.medihelper.presentation.feature.launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.FragmentWelcomeBinding
import com.maruchin.medihelper.presentation.framework.bind

class WelcomeFragment : Fragment() {

    private val directions by lazy { WelcomeFragmentDirections }

    fun onClickNavToMainPersonFragment() = findNavController().navigate(directions.toMainPersonFragment())

    fun onClickNavToLoginFragment() = findNavController().navigate(directions.toLoginFragment())

    fun onClickNavToPatronConnectFragment() = findNavController().navigate(directions.toPatronConnectFragment())

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
}