package com.maruchin.medihelper.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maruchin.medihelper.R
import com.maruchin.medihelper.presentation.framework.AppBottomSheetDialog
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.databinding.DialogSelectExpireDateBinding
import com.maruchin.medihelper.domain.usecases.DateTimeUseCases
import com.maruchin.medihelper.presentation.framework.bind
import kotlinx.android.synthetic.main.dialog_select_expire_date.*
import org.koin.android.ext.android.inject

class SelectExpireDateDialog : AppBottomSheetDialog() {
    override val TAG = "SelectMontDateDialog"

    companion object {
        private const val MIN_YEAR = 1900
        private const val MAX_YEAR = 2100
    }

    var defaultDate: AppExpireDate? = null
    private var dateSelectedListener: ((date: AppExpireDate) -> Unit)? = null
    private val dateTimeService: DateTimeUseCases by inject()

    fun onClickConfirm() {
        val selectedDate = AppExpireDate(year = year_picker.value, month = month_picker.value)
        dateSelectedListener?.invoke(selectedDate)
        dismiss()
    }

    fun setDateSelectedListener(listener: (date: AppExpireDate) -> Unit) {
        dateSelectedListener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bind<DialogSelectExpireDateBinding>(
            inflater = inflater,
            container = container,
            layoutResId = R.layout.dialog_select_expire_date
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
            minValue = MIN_YEAR
            maxValue = MAX_YEAR
            value = defaultDate?.year ?: curDate.year
        }
    }
}