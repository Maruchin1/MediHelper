package com.maruchin.medihelper.domain.usecases.datetime

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate

class GetCurrDateUseCase(
    private val deviceCalendar: DeviceCalendar
) {
    fun execute(): AppDate {
        return AppDate(deviceCalendar.getCurrTimeInMillis())
    }
}