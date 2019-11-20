package com.example.medihelper.domain.usecases

import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import java.util.*

class DateTimeUseCases  {

    fun getCurrDate() = AppDate(Calendar.getInstance().timeInMillis)

    fun getCurrTime() = AppTime(Calendar.getInstance().timeInMillis)

    fun calcDaysBetween(firstDate: AppDate, secondDate: AppDate): Long {
        val days1 = firstDate.timeInMillis / (24 * 3600 * 1000)
        val days2 = secondDate.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }
}