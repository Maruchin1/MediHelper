package com.example.medihelper.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.io.File
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.widget.*
import com.example.medihelper.BR
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


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

@BindingAdapter("android:src")
fun setImageViewSrc(imageView: ImageView, imageResId: Int?) {
    if (imageResId != null) {
        imageView.setImageResource(imageResId)
    }
}

@BindingAdapter("android:background")
fun setBackgroundColor(view: View, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        view.setBackgroundColor(ContextCompat.getColor(view.context, colorResId))
    }
}

@BindingAdapter("cardBackgroundColorResID")
fun setCardBackgroundColor(materialCardView: MaterialCardView, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        materialCardView.setCardBackgroundColor(ContextCompat.getColor(materialCardView.context, colorResId))
    }
}

@BindingAdapter("fabBackgroundColor")
fun setFabColor(fab: FloatingActionButton, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(fab.context, colorResId))
    }
}

@BindingAdapter("android:tint")
fun setImageButtonIconTintColor(imageButton: ImageButton, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        imageButton.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageButton.context, colorResId))
    }
}

@BindingAdapter("android:tint")
fun setImageViewTintColor(imageView: ImageView, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        imageView.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageView.context, colorResId))
    }
}

