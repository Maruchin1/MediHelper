package com.example.medihelper.custom

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("android:layout_weight")
fun setLayoutWeight(view: View, weight: Float?) {
    val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
    if (weight != null) {
        layoutParams?.weight = weight
    }
    view.layoutParams = layoutParams
}