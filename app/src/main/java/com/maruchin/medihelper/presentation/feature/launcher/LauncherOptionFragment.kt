package com.maruchin.medihelper.presentation.feature.launcher

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class LauncherOptionFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(LauncherTransitions()) {
            enterTransition = getOptionEnter()
            returnTransition = getOptionReturn()
            exitTransition = getOptionExit()
            reenterTransition = getOptionReenter()
        }
    }
}