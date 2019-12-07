package com.maruchin.medihelper.presentation.framework

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import java.io.File

//View
@BindingAdapter("android:background")
fun setBackgroundColor(view: View, color: String?) {
    if (!color.isNullOrEmpty()) {
        view.setBackgroundColor(Color.parseColor(color))
    }
}

//CardView
@BindingAdapter("app:cardBackgroundColor")
fun setCardBackgroundColor(materialCardView: MaterialCardView, color: String?) {
    if (!color.isNullOrEmpty()) {
        materialCardView.setCardBackgroundColor(Color.parseColor(color))
    }
}

//TextView
@BindingAdapter("android:textColor")
fun setTextColor(textView: TextView, color: String?) {
    if (!color.isNullOrEmpty()) {
        textView.setTextColor(Color.parseColor(color))
    }
}

//ImageView
@BindingAdapter("android:src")
fun setImageViewSrcFile(imageView: ImageView, imageFile: File?) {
    if (imageFile != null) {
        Glide.with(imageView.context)
            .load(imageFile)
            .centerCrop()
            .into(imageView)
    }
}