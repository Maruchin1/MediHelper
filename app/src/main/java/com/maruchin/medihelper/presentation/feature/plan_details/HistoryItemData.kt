package com.maruchin.medihelper.presentation.feature.plan_details

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.HistoryItem

data class HistoryItemData(
    val date: AppDate,
    val dayOfWeek: String,
    val dayAndMonth: String,
    val currDate: Boolean,
    val checkBoxes: List<CheckBox>
) {
    companion object {

        fun fromDomainModel(model: HistoryItem, currDate: AppDate) =
            HistoryItemData(
                date = model.date,
                dayOfWeek = model.date.dayOfWeekString,
                dayAndMonth = model.date.dayMonthString,
                currDate = model.date == currDate,
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
        val taken: Boolean
    ) {
        companion object {

            fun fromDomainModel(model: HistoryItem.CheckBox) =
                CheckBox(
                    plannedTime = model.plannedTime.formatString,
                    taken = getTaken(model.status)
                )

            private fun getTaken(status: PlannedMedicine.Status) : Boolean {
                return status == PlannedMedicine.Status.TAKEN
            }
        }
    }
}