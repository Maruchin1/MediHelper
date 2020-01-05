package com.maruchin.medihelper.presentation.feature.medicine_plan_details

import com.maruchin.medihelper.domain.entities.TimeDose

data class TimeDoseData(
    val time: String,
    val doseSize: String,
    val colorPrimary: String
) {

    companion object {
        fun fromDomainModel(model: TimeDose, medicineUnit: String, profileColor: String) =
            TimeDoseData(
                time = model.time.formatString,
                doseSize = "${model.doseSize} $medicineUnit",
                colorPrimary = profileColor
            )
    }
}