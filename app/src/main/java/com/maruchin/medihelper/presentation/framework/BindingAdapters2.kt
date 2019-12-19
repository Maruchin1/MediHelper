package com.maruchin.medihelper.presentation.framework

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

@BindingAdapter("android:backgroundTint")
fun setBackgroundTint(view: View, color: String?) {
    if (!color.isNullOrEmpty()) {
        view.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, visible: Boolean?) {
    view.visibility = if (visible == true) View.VISIBLE else View.GONE
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

@BindingAdapter("android:tint")
fun setImageViewTint(imageView: ImageView, color: String?) {
    if (!color.isNullOrEmpty()) {
        imageView.imageTintList = ColorStateList.valueOf(Color.parseColor(color))
    }
}

@BindingAdapter("android:tint")
fun setImageViewTint(imageView: ImageView, colorResId: Int?) {
    if (colorResId != null) {
        imageView.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(imageView. context, colorResId))
    }
}

//ImageButton
@BindingAdapter("android:tint")
fun setImageButtonTint(imageButton: ImageButton, color: String?) {
    if (!color.isNullOrEmpty()) {
        imageButton.imageTintList = ColorStateList.valueOf(Color.parseColor(color))
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

//Fab
@BindingAdapter("app:backgroundTint")
fun setFabBackgroundTint(fab: FloatingActionButton, color: String?) {
    if (!color.isNullOrEmpty()) {
        fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
    }
}

@BindingAdapter("app:backgroundTint")
fun setExFabBackgroundTine(fab: ExtendedFloatingActionButton, color: String?) {
    if (!color.isNullOrEmpty()) {
        fab.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
    }
}

//BottomNav
@BindingAdapter("bottomNavColor")
fun setBottomNavColor(bottomNavigationView: BottomNavigationView, color: String?) {
    if (!color.isNullOrEmpty()) {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_selected), // unchecked
            intArrayOf(android.R.attr.state_selected)  // pressed
        )
        val colors = intArrayOf(
            ContextCompat.getColor(bottomNavigationView.context, R.color.colorTextTertiary),
            Color.parseColor(color)
        )
        val colorStateList = ColorStateList(states, colors)
        bottomNavigationView.run {
            itemIconTintList = colorStateList
            itemTextColor = colorStateList
        }
    }
}