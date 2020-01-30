package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.View
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.R
import com.maruchin.medihelper.databinding.DialogSelectDateBinding
import com.maruchin.medihelper.presentation.framework.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_select_date.*


class SelectDateDialog(
    private val defaultDate: AppDate? = null
) : BaseBottomDialog<DialogSelectDateBinding>(R.layout.dialog_select_date) {
    override val TAG = "SelectDateDialog"

    private var onDateSelectedListener: ((date: AppDate) -> Unit)? = null

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedDate = AppDate(calendar_view.date)
        onDateSelectedListener?.invoke(selectedDate)
        dismiss()
    }

    fun setOnDateSelectedListener(listener: (date: AppDate) -> Unit) {
        onDateSelectedListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        if (defaultDate != null) {
            calendar_view.date = defaultDate.timeInMillis
        }
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            val selectedDate = AppDate(year, month + 1, day)
            calendar_view.date = selectedDate.timeInMillis
        }
    }
}