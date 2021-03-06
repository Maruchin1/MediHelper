package com.maruchin.medihelper.presentation.framework

import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet

class PhotoTransition : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER

        addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addTransition(ChangeImageTransform())
    }
}