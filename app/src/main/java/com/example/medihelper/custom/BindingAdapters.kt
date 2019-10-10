package com.example.medihelper.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import java.io.File
import android.content.res.ColorStateList
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


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
    if (imageFile != null) {
        Glide.with(imageView.context)
            .load(imageFile)
            .centerCrop()
            .into(imageView)
    }
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

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(view: View, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        view.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(view.context, colorResId))
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

@BindingAdapter("inLayError")
fun setTextInputError(inLay: TextInputLayout, errorMessage: String?) {
    inLay.apply {
        error = errorMessage
        isErrorEnabled = errorMessage != null
    }
}

@BindingAdapter("android:text")
fun setFloatText(editText: TextInputEditText, newValue: Float?) {
    if (newValue != null && editText.text.toString() != newValue.toString()) {
        editText.setText(newValue.toString())
    }
}


@BindingAdapter("app:chipBackgroundColor")
fun setChipBackgroundColor(chip: Chip, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        chip.setChipBackgroundColorResource(colorResId)
    }
}

@BindingAdapter("toolbarNavigationOnClick")
fun setToolbarNavigationOnClickListener(toolbar: Toolbar, function: () -> Unit) {
    toolbar.setNavigationOnClickListener { function.invoke() }
}

@BindingAdapter("android:layout_width")
fun setViewWidth(view: View, width: String) {
    view.layoutParams.width = when (width) {
        "match_parent" -> ViewGroup.LayoutParams.MATCH_PARENT
        "wrap_content" -> ViewGroup.LayoutParams.WRAP_CONTENT
        else -> {
            val scale = view.context.resources.displayMetrics.density
            (width.toInt() * scale + 0.5f).toInt()
        }
    }
    view.requestLayout()
}

@BindingAdapter("app:titleTextColor")
fun setToolbarTitleColor(toolbar: Toolbar, colorResId: Int?) {
    if (colorResId != null && colorResId != 0) {
        toolbar.setTitleTextColor(ColorStateList.valueOf(ContextCompat.getColor(toolbar.context, colorResId)))
    }
}