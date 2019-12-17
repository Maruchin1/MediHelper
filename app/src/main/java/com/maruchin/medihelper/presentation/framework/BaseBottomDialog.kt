package com.maruchin.medihelper.presentation.framework

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maruchin.medihelper.BR

abstract class BaseBottomDialog<T : ViewDataBinding>(
    private val layoutResId: Int,
    private val collapsing: Boolean = false
) : BottomSheetDialogFragment() {
    abstract val TAG: String

    protected var bindingViewModel: ViewModel? = null

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: T = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.apply {
            setVariable(BR.handler, this@BaseBottomDialog)
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, bindingViewModel)
        }.root
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.setOnShowListener {
            val bottomDialog = it as BottomSheetDialog
            val bottomSheet = bottomDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val params = bottomSheet?.layoutParams
            params?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet?.layoutParams = params

            if (collapsing) {
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.peekHeight = getHalfScreenHeight()
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun setMinHeight(view: View) {
        view.minimumHeight = getHalfScreenHeight()
    }

    private fun getHalfScreenHeight() = (Resources.getSystem().displayMetrics.heightPixels) / 2
}