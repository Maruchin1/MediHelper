package com.example.medihelper.custom

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.medihelper.AppDateTime
import com.google.android.material.button.MaterialButton
import java.io.File
import java.sql.Time

@BindingAdapter("android:layout_weight")
fun setLayoutWeight(view: View, weight: Float?) {
    val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
    if (weight != null) {
        layoutParams?.weight = weight
    }
    view.layoutParams = layoutParams
}

@BindingAdapter("srcFile")
fun setImageViewFile(imageView: ImageView, imageFile: File?) {
    Glide.with(imageView.context)
        .load(imageFile)
        .centerCrop()
        .into(imageView)
}

@BindingAdapter("android:textColor")
fun setTextColor(textView: TextView, colorResId: Int?) {
    if (colorResId != null) {
        textView.setTextColor(ContextCompat.getColor(textView.context, colorResId))
    }
}

@BindingAdapter("app:icon")
fun setIcon(button: MaterialButton, iconResId: Int?) {
    if (iconResId != null) {
        button.setIconResource(iconResId)
    }
}