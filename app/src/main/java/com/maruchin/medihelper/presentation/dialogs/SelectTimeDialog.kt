package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppBottomSheetDialog
import com.maruchin.medihelper.databinding.DialogSelectTimeBinding
import kotlinx.android.synthetic.main.dialog_select_time.*

class SelectTimeDialog : AppBottomSheetDialog() {
    override val TAG = "SelectTimeDialog"

    var defaultTime: AppTime? = null
    private var timeSelectedListener: ((time: AppTime) -> Unit)? = null

    fun setTimeSelectedListener(listener: (time: AppTime) -> Unit) {
        timeSelectedListener = listener
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedTime =
            AppTime(time_picker.currentHour, time_picker.currentMinute)
        timeSelectedListener?.invoke(selectedTime)
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTimePicker()
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
        defaultTime?.hour?.let { time_picker.currentHour = it }
        defaultTime?.minute?.let { time_picker.currentMinute = it }
    }

    companion object {
        val TAG = SelectTimeDialog::class.simpleName
    }
}