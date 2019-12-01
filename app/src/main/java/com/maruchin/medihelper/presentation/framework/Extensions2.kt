package com.maruchin.medihelper.presentation.framework

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
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