package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.View
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogSelectNumberBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_select_number.*

class SelectNumberDialog(
    val title: String,
    val iconResId: Int,
    val defaultNumber: Int?
) : BaseBottomDialog<DialogSelectNumberBinding>(R.layout.dialog_select_number) {
    override val TAG = "SelectNumberDialog"

    private var onNumberSelectedListener: ((number: Int) -> Unit)? = null

    fun setOnNumberSelectedListener(listener: (number: Int) -> Unit) {
        onNumberSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedNumber = number_picker.value
        onNumberSelectedListener?.invoke(selectedNumber)
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPicker()
    }

    private fun setupNumberPicker() {
        number_picker.minValue = 0
        number_picker.maxValue = 100
        if (defaultNumber != null) {
            number_picker.value = defaultNumber
        }
    }
}