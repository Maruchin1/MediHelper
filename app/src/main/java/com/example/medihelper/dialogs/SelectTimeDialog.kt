package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogSelectTimeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_time.*
import java.sql.Time

class SelectTimeDialog : BottomSheetDialogFragment() {

    var defaultTime: Time? = null
    private var timeSelectedListener: ((time: Time) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTimePicker()
    }

    fun setTimeSelectedListener(listener: (time: Time) -> Unit) {
        timeSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedTime = Time(time_picker.currentHour, time_picker.currentMinute, 0)
        timeSelectedListener?.invoke(selectedTime)
        dismiss()
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogSelectTimeBinding = DataBindingUtil.inflate(inflater,
            R.layout.dialog_select_time, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupTimePicker() {
        time_picker.setIs24HourView(true)
        defaultTime?.hours?.let { time_picker.currentHour = it }
        defaultTime?.minutes?.let { time_picker.currentMinute = it }
    }

    companion object {
        val TAG = SelectTimeDialog::class.simpleName
    }
}