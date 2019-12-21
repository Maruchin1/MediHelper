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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maruchin.medihelper.BR
import com.maruchin.medihelper.data.ProfileColor
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import org.koin.android.ext.android.inject

abstract class BaseBottomDialog<T : ViewDataBinding>(
    private val layoutResId: Int,
    private val collapsing: Boolean = false
) : BottomSheetDialogFragment() {
    abstract val TAG: String

    val colorPrimary: LiveData<String>
        get() = _colorPrimary
    protected var bindingViewModel: ViewModel? = null
    private val _colorPrimary = MutableLiveData<String>(ProfileColor.MAIN.colorString)

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    fun setColorPrimary(colorString: String?) {
        _colorPrimary.value = colorString
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
        if (!collapsing) {
            setupStandardBehavior(view)
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        if (collapsing) {
            setupCollapsingBehavior(dialog)
        }
    }

    private fun setupCollapsingBehavior(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomDialog = it as BottomSheetDialog
            val bottomSheet = bottomDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val params = bottomSheet?.layoutParams
            params?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet?.layoutParams = params

            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.peekHeight = getHalfScreenHeight()
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun setupStandardBehavior(view: View) {
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

    private fun setMinHeight(view: View) {
        view.minimumHeight = getHalfScreenHeight()
    }

    private fun getHalfScreenHeight() = (Resources.getSystem().displayMetrics.heightPixels) / 2
}