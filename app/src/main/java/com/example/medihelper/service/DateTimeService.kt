package com.example.medihelper.service

import com.example.medihelper.localdatabase.AppDate
import com.example.medihelper.localdatabase.AppTime
import java.util.*

interface DateTimeService {
    fun getCurrDate(): AppDate
    fun getCurrTime(): AppTime
    fun daysBetween(date1: AppDate, date2: AppDate): Long
}

class DateTimeServiceImpl : DateTimeService {

    override fun getCurrDate() = AppDate(Calendar.getInstance().timeInMillis)

    override fun getCurrTime() = AppTime(Calendar.getInstance().timeInMillis)

    override fun daysBetween(date1: AppDate, date2: AppDate): Long {
        val days1 = date1.timeInMillis / (24 * 3600 * 1000)
        val days2 = date2.timeInMillis / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }
}