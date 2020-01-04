package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.model.MedicinePlanDetails

data class MedicinePlanDaysData(
    val daysType: String,
    val daysDetails: String?
) {

    companion object {

        fun fromDomainModel(model: MedicinePlanDetails): MedicinePlanDaysData? {
            return when (model.intakeDays) {
                is IntakeDays.Everyday -> getForEveryday()
                is IntakeDays.DaysOfWeek -> getForDaysOfWeek(model.intakeDays)
                is IntakeDays.Interval -> getForInterval(model.intakeDays)
                is IntakeDays.Sequence -> getForSequence(model.intakeDays)
                else -> null
            }
        }

        private fun getForEveryday() = MedicinePlanDaysData(
            daysType = "Codziennie",
            daysDetails = null
        )

        private fun getForDaysOfWeek(intakeDays: IntakeDays.DaysOfWeek) = MedicinePlanDaysData(
            daysType = "Wybrane dni tygodnia",
            daysDetails = getDaysOfWeekData(intakeDays)
        )

        private fun getForInterval(intakeDays: IntakeDays.Interval) = MedicinePlanDaysData(
            daysType = "Co ${intakeDays.daysCount} dni",
            daysDetails = null
        )

        private fun getForSequence(intakeDays: IntakeDays.Sequence) = MedicinePlanDaysData(
            daysType = "Sekwencja dni",
            daysDetails = "${intakeDays.intakeCount} dni przyjmowania,\n${intakeDays.notIntakeCount} dni przerwy"
        )

        private fun getDaysOfWeekData(daysOfWeek: IntakeDays.DaysOfWeek): String {
            val builder = StringBuilder()
                .append(if (daysOfWeek.monday) "po, " else "")
                .append(if (daysOfWeek.tuesday) "wt, " else "")
                .append(if (daysOfWeek.wednesday) "śr, " else "")
                .append(if (daysOfWeek.thursday) "cz, " else "")
                .append(if (daysOfWeek.friday) "pi, " else "")
                .append(if (daysOfWeek.saturday) "so, " else "")
                .append(if (daysOfWeek.sunday) "nd, " else "")
            return builder.toString().apply {
                dropLast(2)
            }
        }
    }
}