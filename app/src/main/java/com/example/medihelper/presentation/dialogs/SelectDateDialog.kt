package com.example.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.R
import com.example.medihelper.presentation.framework.AppBottomSheetDialog
import com.example.medihelper.databinding.DialogSelectDateBinding
import com.example.medihelper.presentation.framework.bind
import kotlinx.android.synthetic.main.dialog_select_date.*


class SelectDateDialog : AppBottomSheetDialog() {
    override val TAG = "SelectDateDialog"

    var defaultDate: AppDate? = null
    private var dateSelectedListener: ((date: AppDate) -> Unit)? = null

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedDate = AppDate(calendar_view.date)
        dateSelectedListener?.invoke(selectedDate)
        dismiss()
    }

    fun setDateSelectedListener(listener: (date: AppDate) -> Unit) {
        dateSelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogSelectDateBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_select_date
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun setupCalendar() {
        if (defaultDate != null) {
            calendar_view.date = defaultDate!!.timeInMillis
        }
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            val selectedDate = AppDate(year, month + 1, day)
            calendar_view.date = selectedDate.timeInMillis
        }
    }
}