package com.example.medihelper.mainapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.databinding.DialogSelectFloatNumberBinding
import kotlinx.android.synthetic.main.dialog_select_float_number.*

class SelectFloatNumberDialog : AppBottomSheetDialog() {
    override val TAG = "SelectFloatNumberDialog"

    var title: String? = null
    var iconResID: Int? = null
    var defaultNumber: Float? = null
    private var numberSelectedListener: ((number: Float) -> Unit)? = null

    fun setNumberSelectedListener(listener: (number: Float) -> Unit) {
        numberSelectedListener = listener
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
        numberSelectedListener?.invoke(selectedNumber)
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogSelectFloatNumberBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_select_float_number, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}