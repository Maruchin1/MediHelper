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

    var defaultNumber: Int? = null
    private var numberSelectedListener: ((number: Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPicker()
    }

    fun setNumberSelectedListener(listener: (number: Int) -> Unit) {
        numberSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedNumber = number_picker.value
        numberSelectedListener?.invoke(selectedNumber)
        dismiss()
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogSelectNumberBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_select_number, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
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