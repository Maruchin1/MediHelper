package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.View
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogSelectFloatNumberBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_select_float_number.*

class SelectFloatNumberDialog(
    val title: String,
    val iconResId: Int,
    val defaultNumber: Float?
) : BaseBottomDialog<DialogSelectFloatNumberBinding>(R.layout.dialog_select_float_number) {
    override val TAG = "SelectFloatNumberDialog"

    private var onNumberSelectedListener: ((number: Float) -> Unit)? = null

    fun setOnNumberSelectedListener(listener: (number: Float) -> Unit) {
        onNumberSelectedListener = listener
    }

    fun onClickIncrementNumber() {
        val currValue = etx_number.text?.toString()?.toFloat() ?: 0f
        val newValue = currValue + 1
        etx_number.setText(newValue.toString())
    }

    fun onClickDecrementNumber() {
        val currValue = etx_number.text?.toString()?.toFloat() ?: 0f
        val newValue = currValue - 1
        if (newValue >= 0) {
            etx_number.setText(newValue.toString())
        }
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedNumber = etx_number.text?.toString()?.toFloat() ?: 0f
        onNumberSelectedListener?.invoke(selectedNumber)
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPicker()
    }

    private fun setupNumberPicker() {
        if (defaultNumber != null) {
            etx_number.setText(defaultNumber.toString())
        }
    }
}