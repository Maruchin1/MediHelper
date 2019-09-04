package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogSelectNumberBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_number.*

class SelectNumberDialog : BottomSheetDialogFragment() {

    var title: String? = null
    var iconResID: Int? = null
    var defaultNumber: Float? = null
    private var numberSelectedListener: ((number: Float) -> Unit)? = null

    fun setNumberSelectedListener(listener: (number: Float) -> Unit) {
        numberSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedNumber = number_picker.value
        numberSelectedListener?.invoke(selectedNumber.toFloat())
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
        defaultNumber?.let { number_picker.value = it.toInt() }
    }

    companion object {
        val TAG = SelectNumberDialog::class.simpleName
    }
}