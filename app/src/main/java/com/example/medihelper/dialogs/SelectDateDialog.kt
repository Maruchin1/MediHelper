package com.example.medihelper.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.example.medihelper.AppDate
import com.example.medihelper.R
import com.example.medihelper.custom.AppBottomSheetDialog
import com.example.medihelper.databinding.DialogSelectDateBinding
import kotlinx.android.synthetic.main.dialog_select_date.*


class SelectDateDialog : AppBottomSheetDialog() {
    override val TAG = "SelectDateDialog"

    var defaultDate: AppDate? = null
    val colorPrimaryLive = MutableLiveData<Int>()
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

    fun setColorPrimary(colorResId: Int) {
        colorPrimaryLive.value = colorResId
    }

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

    private fun setupCalendar() {
        if (defaultDate != null) {
            calendar_view.date = defaultDate!!.timeInMillis
        }
        calendar_view.setOnDateChangeListener { _, year, month, day ->
            val selectedDate = AppDate(year, month, day)
            calendar_view.date = selectedDate.timeInMillis
        }
    }
}