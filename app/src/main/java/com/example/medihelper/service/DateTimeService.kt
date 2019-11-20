package com.example.medihelper.service

import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import java.util.*

interface DateTimeService {
    fun getCurrDate(): AppDate
    fun getCurrTime(): AppTime
    fun calcDaysBetween(firstDate: AppDate, secondDate: AppDate): Long
}

class DateTimeServiceImpl : DateTimeService {

    override fun getCurrDate() = AppDate(Calendar.getInstance().timeInMillis)

    override fun getCurrTime() = AppTime(Calendar.getInstance().timeInMillis)

    override fun calcDaysBetween(firstDate: AppDate, secondDate: AppDate): Long {
        val days1 = firstDate.timeInMillis / (24 * 3600 * 1000)
        val days2 = secondDate.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }
}