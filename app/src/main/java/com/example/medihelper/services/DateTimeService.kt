package com.example.medihelper.services

import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import java.util.*

class DateTimeService {

    fun getCurrDate() = AppDate(Calendar.getInstance().timeInMillis)

    fun getCurrTime() = AppTime(Calendar.getInstance().timeInMillis)

    fun daysBetween(date1: AppDate, date2: AppDate): Long {
        val days1 = date1.timeInMillis / (24 * 3600 * 1000)
        val days2 = date2.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }
}