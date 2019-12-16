package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.TimeDose

data class TimeDoseDb(
    val time: String? = null,
    val doseSize: Float? = null
) {
    constructor(entity: TimeDose) : this(
        time = entity.time.jsonFormatString,
        doseSize = entity.doseSize
    )

    fun toEntity() = TimeDose(
        time = AppTime(time!!),
        doseSize = doseSize!!
    )
}