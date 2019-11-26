package com.maruchin.medihelper.presentation.framework

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.ViewTreeObserver



abstract class AppBottomSheetDialog : BottomSheetDialogFragment() {
    abstract val TAG: String
    val colorPrimaryLive = MutableLiveData<Int>()

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    fun setColorPrimary(colorResId: Int) {
        colorPrimaryLive.value = colorResId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog?
                val bottomSheet = dialog!!.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })
    }
}