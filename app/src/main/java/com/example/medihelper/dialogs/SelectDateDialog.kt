package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.AppDateTime
import com.example.medihelper.R
import com.example.medihelper.databinding.DialogSelectDateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_date.*
import java.util.*

class SelectDateDialog : BottomSheetDialogFragment() {
    val TAG = SelectDateDialog::class.simpleName

    var defaultDate: Date? = null
    private var dateSelectedListener: ((date: Date) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: DialogSelectDateBinding = DataBindingUtil.inflate(inflater,
            R.layout.dialog_select_date, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        val selectedDate = AppDateTime.makeDate(calendar_view.date)
        dateSelectedListener?.invoke(selectedDate)
        dismiss()
    }

    fun setDateSelectedListener(listener: (date: Date) -> Unit) {
        dateSelectedListener = listener
    }

    private fun setupCalendar() {
        defaultDate?.let { calendar_view.date = it.time }
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            calendar_view.date = AppDateTime.makeDate(day, month, year).time
        }
    }
}