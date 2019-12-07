package com.maruchin.medihelper.domain.usecases

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime

class DateTimeUseCases(
    private val deviceCalendar: DeviceCalendar
)  {

    fun getCurrDate() = AppDate(deviceCalendar.getCurrTimeInMillis())

    fun getCurrTime() = AppTime(deviceCalendar.getCurrTimeInMillis())

    fun calcDaysBetween(firstDate: AppDate, secondDate: AppDate): Long {
        val days1 = firstDate.timeInMillis / (24 * 3600 * 1000)
        val days2 = secondDate.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }
}