package com.maruchin.medihelper.presentation.framework

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.R

abstract class BaseBottomDialog<T : ViewDataBinding>(
    private val layoutResId: Int,
    private val collapsing: Boolean = false
) : BottomSheetDialogFragment() {
    abstract val TAG: String

    protected var bindingViewModel: ViewModel? = null

    private lateinit var behavior: BottomSheetBehavior<View>

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    protected fun setCollapsed() {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: T = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.apply {
            setVariable(BR.handler, this@BaseBottomDialog)
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, bindingViewModel)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (collapsing) {
            setMinHeight(view)
        } else {
            setupFullExpand(view)
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (collapsing) {
            setupCollapsing(dialog)
        }
    }

    private fun setMinHeight(view: View) {
        view.minimumHeight = getHaflScreenHeight()
    }

    private fun setupCollapsing(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomDialog = it as BottomSheetDialog
            val bottomSheet = bottomDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            behavior = BottomSheetBehavior.from(bottomSheet!!)


            behavior.peekHeight = getHaflScreenHeight()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setupFullExpand(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val dialog = dialog as BottomSheetDialog?
                val bottomSheet =
                    dialog!!.findViewById(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })
    }

    private fun getHaflScreenHeight() = (Resources.getSystem().displayMetrics.heightPixels) / 2
}