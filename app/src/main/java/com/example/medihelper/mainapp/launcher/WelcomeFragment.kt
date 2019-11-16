package com.example.medihelper.mainapp.launcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.example.medihelper.R
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    companion object {
        private const val ANIM_TIME = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getSharedElemTransition()
        enterTransition = getSlideFadeEnterTransition()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<FragmentWelcomeBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.fragment_welcome
        )
    }

    private fun getSharedElemTransition() = TransitionSet().apply {
        addTransition(TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move))
        duration = ANIM_TIME
    }

    private fun getSlideFadeEnterTransition() = Fade().apply {
        duration = 500L
        startDelay = ANIM_TIME
    }
}