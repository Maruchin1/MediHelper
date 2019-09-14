package com.example.medihelper.custom

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AppBottomSheetDialog : BottomSheetDialogFragment() {
    abstract val TAG: String

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)
}