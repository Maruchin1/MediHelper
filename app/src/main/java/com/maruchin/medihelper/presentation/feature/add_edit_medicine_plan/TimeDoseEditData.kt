package com.maruchin.medihelper.presentation.feature.add_edit_medicine_plan

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.TimeDose

data class TimeDoseEditData(
    val position: Int,
    val time: AppTime,
    val doseSize: Float
) {
    companion object {

        fun fromDomainModel(model: TimeDose, position: Int) =
            TimeDoseEditData(
                position = position,
                time = model.time,
                doseSize = model.doseSize
            )
    }

    fun toDomainModel() = TimeDose(
        time = time,
        doseSize = doseSize
    )
}