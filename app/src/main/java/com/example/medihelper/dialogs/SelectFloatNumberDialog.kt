package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogSelectFloatNumberBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectFloatNumberDialog : BottomSheetDialogFragment() {

    var title: String? = null
    var iconResID: Int? = null
    var defaultNumber: Float? = null
    val selectedNumberLive = MutableLiveData(1f)
    private var numberSelectedListener: ((number: Float) -> Unit)? = null

    fun setNumberSelectedListener(listener: (number: Float) -> Unit) {
        numberSelectedListener = listener
    }

    fun onClickIncrementNumber() {
        selectedNumberLive.value?.let { currValue ->
            selectedNumberLive.value = currValue + 1
        }
    }

    fun onClickDecrementNumber() {
        selectedNumberLive.value?.let { currValue ->
            val newValue = currValue - 1f
            if (newValue >= 0) {
                selectedNumberLive.value = newValue
            }
        }
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        selectedNumberLive.value?.let { selectedNumber ->
            numberSelectedListener?.invoke(selectedNumber)
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogSelectFloatNumberBinding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_select_float_number, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}