package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogSelectTimeBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_select_time.*

class SelectTimeDialog(
    private val defaultTime: AppTime? = null
) : BaseBottomDialog<DialogSelectTimeBinding>(R.layout.dialog_select_time) {
    override val TAG = "SelectTimeDialog"

    private var onTimeSelectedListener: ((time: AppTime) -> Unit)? = null

    fun setOnTimeSelectedListener(listener: (time: AppTime) -> Unit) {
        onTimeSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedTime = AppTime(time_picker.currentHour, time_picker.currentMinute)
        onTimeSelectedListener?.invoke(selectedTime)
        dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTimePicker()
    }

    private fun setupTimePicker() {
        time_picker.setIs24HourView(true)
        defaultTime?.hour?.let { time_picker.currentHour = it }
        defaultTime?.minute?.let { time_picker.currentMinute = it }
    }
}