package com.example.medihelper.domain.usecases

import com.example.medihelper.domain.deviceapi.CalendarApi
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime

class DateTimeUseCases(
    private val calendarApi: CalendarApi
)  {

    fun getCurrDate() = AppDate(calendarApi.getCurrTimeInMillis())

    fun getCurrTime() = AppTime(calendarApi.getCurrTimeInMillis())

    fun calcDaysBetween(firstDate: AppDate, secondDate: AppDate): Long {
        val days1 = firstDate.timeInMillis / (24 * 3600 * 1000)
        val days2 = secondDate.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }
}