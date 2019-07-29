package com.example.medihelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_date.*

class SelectDateDialog : BottomSheetDialogFragment() {

    var resultSelectedDateStringLive: MutableLiveData<String>? = null
    private var selectedDateStringLive = MutableLiveData<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_date, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedDate()
        setupCalendar()
        setupButtons()
    }

    private fun observeSelectedDate() {
        selectedDateStringLive.observe(viewLifecycleOwner, Observer { selectedDate ->
            if (selectedDate != null) {
                txv_selected_date.text = selectedDate
            } else {
                txv_selected_date.text = "Nie określono"
            }
        })
    }

    private fun setupCalendar() {
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            val selectedDate = AppDateTimeUtil.makeDate(day, month, year)
            selectedDateStringLive.value = AppDateTimeUtil.dateToString(selectedDate)
        }
        val selectedDateString = resultSelectedDateStringLive?.value
        if (selectedDateString != null) {
            calendar_view.date = AppDateTimeUtil.stringToDate(selectedDateString).time
            selectedDateStringLive.value = selectedDateString
        } else {
            val currDate = AppDateTimeUtil.getCurrCalendar().time
            calendar_view.date = currDate.time
            selectedDateStringLive.value = AppDateTimeUtil.dateToString(currDate)
        }
    }

    private fun setupButtons() {
        btn_confirm.setOnClickListener {
            resultSelectedDateStringLive?.value = selectedDateStringLive.value
            dismiss()
        }
        btn_cancel.setOnClickListener { dismiss() }
    }

    companion object {
        val TAG = SelectDateDialog::class.simpleName
    }
}