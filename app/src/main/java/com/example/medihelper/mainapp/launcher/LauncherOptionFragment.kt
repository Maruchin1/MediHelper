package com.example.medihelper.mainapp.launcher

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class LauncherOptionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(LauncherTransitions()) {
            sharedElementEnterTransition = getOptionEnterShared(requireContext())
            enterTransition = getOptionEnter()
            returnTransition = getOptionReturn()
            exitTransition = getOptionExit()
            reenterTransition = getOptionReenter()
        }
    }
}