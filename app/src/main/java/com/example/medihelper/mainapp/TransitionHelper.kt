package com.example.medihelper.mainapp

import android.content.Context
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet

class TransitionHelper {

    fun sharedElementEnterTransition(context: Context): TransitionSet {
        return TransitionSet().apply {
            addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
            duration = SHARED_ELEMENT_TRANSITION_DURATION
        }
    }

    companion object {
        const val SHARED_ELEMENT_TRANSITION_DURATION = 300L
    }
}