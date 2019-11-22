package com.example.medihelper.presentation.feature.launcher

import android.content.Context
import android.view.Gravity
import androidx.transition.*

class LauncherTransitions {

    fun getWelcomeEnterShared(context: Context) = TransitionSet().apply {
        addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
        duration = 1000
    }

    fun getWelcomeEnter() = Fade().apply {
        duration = 500
        startDelay = 1000
    }

    fun getWelcomeExit() = Slide(Gravity.BOTTOM).apply {
        duration = 300
    }

    fun getWelcomeReenter() = Slide(Gravity.BOTTOM).apply {
        duration = 300
        startDelay = 600
    }

    fun getOptionEnterShared(context: Context) = TransitionSet().apply {
        addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.no_transition))
        duration = 0
    }

    fun getOptionEnter() = Slide(Gravity.BOTTOM).apply {
        duration = 300
        startDelay = 600
    }

    fun getOptionExit() = Slide(Gravity.BOTTOM).apply {
        duration = 300
    }

    fun getOptionReturn() = Slide(Gravity.BOTTOM).apply {
        duration = 300
    }

    fun getOptionReenter() = Slide(Gravity.BOTTOM).apply {
        duration = 300
        startDelay = 600
    }
}