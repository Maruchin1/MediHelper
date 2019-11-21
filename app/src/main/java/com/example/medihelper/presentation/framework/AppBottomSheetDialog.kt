package com.example.medihelper.presentation.framework

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AppBottomSheetDialog : BottomSheetDialogFragment() {
    abstract val TAG: String
    val colorPrimaryLive = MutableLiveData<Int>()

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    fun setColorPrimary(colorResId: Int) {
        colorPrimaryLive.value = colorResId
    }
}