package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.databinding.DialogSelectExpireDateBinding
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_select_expire_date.*
import org.koin.android.ext.android.inject

class SelectExpireDateDialog(
    private val defaultDate: AppExpireDate? = null
) : BaseBottomDialog<DialogSelectExpireDateBinding>(R.layout.dialog_select_expire_date) {
    override val TAG = "SelectMontDateDialog"

    companion object {
        private const val MIN_YEAR = 1900
        private const val MAX_YEAR = 2100
    }

    private var onDateSelectedListener: ((date: AppExpireDate) -> Unit)? = null
    private val deviceCalendar: DeviceCalendar by inject()

    fun onClickConfirm() {
        val selectedDate = AppExpireDate(year = year_picker.value, month = month_picker.value)
        onDateSelectedListener?.invoke(selectedDate)
        dismiss()
    }

    fun setOnDateSelectedListener(listener: (date: AppExpireDate) -> Unit) {
        onDateSelectedListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPickers()
    }

    private fun setupNumberPickers() {
        val curDate = deviceCalendar.getCurrDate()
        month_picker.run {
            minValue = 1
            maxValue = 12
            value = (defaultDate?.month ?: curDate.month) + 1
        }
        year_picker.run {
            minValue = MIN_YEAR
            maxValue = MAX_YEAR
            value = defaultDate?.year ?: curDate.year
        }
    }
}