package com.example.medihelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.medihelper.databinding.DialogSelectDateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_select_date.*
import java.util.*

class SelectDateDialog : BottomSheetDialogFragment() {

    var selectedDate: Date? = null
    private var dateSelectedListener: ((date: Date) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindLayout(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    fun onClickCancel() = dismiss()

    fun onClickConfirm() {
        selectedDate?.let { dateSelectedListener?.invoke(it) }
        dismiss()
    }

    fun setDateSelectedListener(listener: (date: Date) -> Unit) {
        dateSelectedListener = listener
    }

    private fun bindLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: DialogSelectDateBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_date, container, false)
        binding.handler = this
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun setupCalendar() {
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            selectedDate = AppDateTimeUtil.makeDate(day, month, year)
        }
        if (selectedDate == null) {
            selectedDate = AppDateTimeUtil.getCurrCalendar().time
        }
        calendar_view.date = selectedDate!!.time
    }

    companion object {
        val TAG = SelectDateDialog::class.simpleName
    }
}