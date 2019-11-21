package com.example.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.presentation.framework.AppBottomSheetDialog
import com.example.medihelper.databinding.DialogConfirmBinding

class ConfirmDialog : AppBottomSheetDialog() {
    override val TAG = "ConfirmDialog"

    var title = ""
    var message = ""
    var iconResId: Int? = null

    private var onConfirmClickListener: (() -> Unit)? = null
    private var onCancelClickListener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogConfirmBinding = DataBindingUtil.inflate(inflater,
            R.layout.dialog_confirm, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun setOnConfirmClickListener(listener: () -> Unit) {
        onConfirmClickListener = listener
    }

    fun setOnCancelClickListener(listener: () -> Unit) {
        onCancelClickListener = listener
    }

    fun onClickConfirm() {
        onConfirmClickListener?.invoke()
        dismiss()
    }

    fun onClickCancel() {
        onCancelClickListener?.invoke()
        dismiss()
    }

    companion object {
        val TAG = ConfirmDialog::class.simpleName
    }
}