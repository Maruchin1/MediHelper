package com.maruchin.medihelper.presentation.feature.medicine_plan_details

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.HistoryItem

data class HistoryItemData(
    val date: AppDate,
    val dayOfWeek: String,
    val dayAndMonth: String,
    val checkBoxes: List<CheckBox>
) {
    companion object {

        fun fromDomainModel(model: HistoryItem) =
            HistoryItemData(
                date = model.date,
                dayOfWeek = model.date.dayOfWeekString,
                dayAndMonth = model.date.dayMonthString,
                checkBoxes = getCheckBoxesData(
                    model.checkboxesList
                )
            )

        private fun getCheckBoxesData(checkboxes: List<HistoryItem.CheckBox>): List<CheckBox> {
            return checkboxes.map { checkBox ->
                CheckBox.fromDomainModel(
                    checkBox
                )
            }
        }
    }

    data class CheckBox(
        val plannedTime: String,
        val statusColorId: Int
    ) {
        companion object {

            fun fromDomainModel(model: HistoryItem.CheckBox) =
                CheckBox(
                    plannedTime = model.plannedTime.formatString,
                    statusColorId = getStatusColor(
                        model.status
                    )
                )

            private fun getStatusColor(status: PlannedMedicine.Status) = when (status) {
                PlannedMedicine.Status.TAKEN -> R.color.colorStateGood
                PlannedMedicine.Status.NOT_TAKEN -> R.color.colorDarkerGray
            }
        }
    }
}