package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.HistoryItem

data class HistoryItem(
    val date: AppDate,
    val dayOfWeek: String,
    val dayAndMonth: String,
    val checkBoxes: List<CheckBox>
) {
    constructor(domainModel: HistoryItem) : this(
        date = domainModel.date,
        dayOfWeek = domainModel.date.dayOfWeekString,
        dayAndMonth = domainModel.date.dayMonthString,
        checkBoxes = domainModel.checkboxesList.map {
            CheckBox(it)
        }
    )

    data class CheckBox(
        val plannedTime: String,
        val statusColorId: Int
    ) {
        constructor(domainModel: HistoryItem.CheckBox) : this(
            plannedTime = domainModel.plannedTime.formatString,
            statusColorId = when (domainModel.status) {
                PlannedMedicine.Status.TAKEN -> R.color.colorStateGood
                PlannedMedicine.Status.NOT_TAKEN -> R.color.colorStateSmall
            }
        )
    }
}