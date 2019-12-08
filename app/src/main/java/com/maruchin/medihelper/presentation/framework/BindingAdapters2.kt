package com.maruchin.medihelper.presentation.framework

import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.R
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

@BindingAdapter("adapter")
fun setMenuAdapter(autoCompleteTextView: AutoCompleteTextView, items: List<String>?) {
    if (items != null) {
        val adapter = ArrayAdapter(autoCompleteTextView.context, R.layout.item_dropdown_menu, items.toTypedArray())
        autoCompleteTextView.setAdapter(adapter)
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

@BindingAdapter("android:src")
fun setImageViewStorageRef(imageView: ImageView, storageReference: StorageReference?) {
    if (storageReference != null) {
        Glide.with(imageView.context)
            .load(storageReference)
            .into(imageView)
    }
}

//TextInput
@BindingAdapter("inLayError")
fun setTextInputError(inLay: TextInputLayout, errorMessage: String?) {
    inLay.apply {
        error = errorMessage
        isErrorEnabled = errorMessage != null
    }
}

//SeekBar
@BindingAdapter("android:max")
fun setSeekBarMax(seekBar: SeekBar, value: Float?) {
    if (value != null) {
        seekBar.max = value.toInt()
    }
}

@BindingAdapter("android:progress")
fun setSeekBarProgress(seekBar: SeekBar, value: Float?) {
    seekBar.progress = value?.toInt() ?: 0
}

@InverseBindingAdapter(attribute = "android:progress")
fun getSeekBarProgress(seekBar: SeekBar): Float? {
    return seekBar.progress.toFloat()
}