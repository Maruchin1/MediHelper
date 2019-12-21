package com.maruchin.medihelper.presentation.dialogs

import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogConfirmBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog

class ConfirmDialog(
    val title: String,
    val message: String,
    val iconResId: Int
) : BaseBottomDialog<DialogConfirmBinding>(R.layout.dialog_confirm) {
    override val TAG = "ConfirmDialog"

    private var onConfirmClickListener: (() -> Unit)? = null

    fun setOnConfirmClickListener(listener: () -> Unit) {
        onConfirmClickListener = listener
    }

    fun onClickConfirm() {
        onConfirmClickListener?.invoke()
        dismiss()
    }

    fun onClickCancel() {
        dismiss()
    }
}