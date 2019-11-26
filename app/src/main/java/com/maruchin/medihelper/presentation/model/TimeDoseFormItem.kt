package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.TimeDose

data class TimeDoseFormItem(
    val time: AppTime,
    val doseSize: Float,
    val medicineUnit: String
) {
    constructor(timeDose: TimeDose, medicineUnit: String?) : this(
        time = timeDose.time,
        doseSize = timeDose.doseSize,
        medicineUnit = medicineUnit ?: "--"
    )

    fun toTimeDose() = TimeDose(
        time = time,
        doseSize = doseSize
    )
}