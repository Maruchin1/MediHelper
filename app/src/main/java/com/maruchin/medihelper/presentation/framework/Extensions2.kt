package com.maruchin.medihelper.presentation.framework

import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.hideOnScroll(recyclerView: RecyclerView) {
    recyclerView.addOnScrollListener(object  : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) hide() else show()
            super.onScrolled(recyclerView, dx, dy)
        }
    })
}

fun ExtendedFloatingActionButton.shrinkOnScroll(nestedScrollView: NestedScrollView) {
    nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
        if (scrollY != 0) shrink() else extend()
    }
}

fun ViewGroup.beginDelayedTransition() {
    TransitionManager.beginDelayedTransition(this)
}

fun ViewGroup.beginDelayedFade() {
    val fade = Fade().apply {
        duration = 200
    }
    TransitionManager.beginDelayedTransition(this, fade)
}