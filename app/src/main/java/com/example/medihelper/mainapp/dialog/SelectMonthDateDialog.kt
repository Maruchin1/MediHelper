package com.example.medihelper.mainapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.custom.AppDate
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.custom.bind
import com.example.medihelper.databinding.DialogSelectMonthDateBinding
import com.example.medihelper.service.DateTimeService
import kotlinx.android.synthetic.main.dialog_select_month_date.*
import org.koin.android.ext.android.inject

class SelectMonthDateDialog : AppBottomSheetDialog() {
    override val TAG = "SelectMontDateDialog"

    companion object {
        private const val MAX_YEAR = 2100
    }

    var defaultDate: AppDate? = null
    private var dateSelectedListener: ((date: AppDate) -> Unit)? = null
    private val dateTimeService: DateTimeService by inject()

    fun onClickConfirm() {
        val selectedDate =
            AppDate(year = year_picker.value, month = month_picker.value - 1, day = 1)
        dateSelectedListener?.invoke(selectedDate)
        dismiss()
    }

    fun setDateSelectedListener(listener: (date: AppDate) -> Unit) {
        dateSelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogSelectMonthDateBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_select_month_date
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPickers()
    }

    private fun setupNumberPickers() {
        val curDate = dateTimeService.getCurrDate()
        month_picker.run {
            minValue = 1
            maxValue = 12
            value = (defaultDate?.month ?: curDate.month) + 1
        }
        year_picker.run {
            minValue = curDate.year
            maxValue = MAX_YEAR
            value = defaultDate?.year ?: curDate.year
        }
    }
}