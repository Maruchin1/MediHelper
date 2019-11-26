package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppBottomSheetDialog
import com.maruchin.medihelper.databinding.DialogSelectNumberBinding
import kotlinx.android.synthetic.main.dialog_select_number.*

class SelectNumberDialog : AppBottomSheetDialog() {
    override val TAG = "SelectNumberDialog"

    var title: String? = null
    var iconResID: Int? = null
    var defaultNumber: Int? = null
    private var numberSelectedListener: ((number: Int) -> Unit)? = null

    fun setNumberSelectedListener(listener: (number: Int) -> Unit) {
        numberSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedNumber = number_picker.value
        numberSelectedListener?.invoke(selectedNumber)
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogSelectNumberBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_select_number, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPicker()
    }

    private fun setupNumberPicker() {
        number_picker.minValue = 0
        number_picker.maxValue = 100
        defaultNumber?.let { number_picker.value = it }
    }

    companion object {
        val TAG = SelectNumberDialog::class.simpleName
    }
}