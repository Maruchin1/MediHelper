package com.example.medihelper.custom

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import java.io.File

@BindingAdapter("android:layout_weight")
fun setLayoutWeight(view: View, weight: Float?) {
    val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
    if (weight != null) {
        layoutParams?.weight = weight
    }
    view.layoutParams = layoutParams
}

@BindingAdapter("srcFile")
fun setImageViewFile(imageView: ImageView, imageFile: File) {
    Glide.with(imageView.context)
        .load(imageFile)
        .centerCrop()
        .into(imageView)
}