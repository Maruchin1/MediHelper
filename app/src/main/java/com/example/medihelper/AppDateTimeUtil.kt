package com.example.medihelper

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object AppDateTimeUtil {

    // Date
    fun getCurrCalendar(): Calendar = Calendar.getInstance()

    fun dateToString(date: Date): String = dateFormat().format(date)

    fun stringToDate(string: String): Date = dateFormat().parse(string)

    fun dayMonthString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun dayOfWeekString(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun makeDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.time
    }

    fun daysBetween(date1: Date, date2: Date): Int {
        val timeDiff = date2.time - date1.time
        return TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS).toInt()
    }

    fun areDatesEqual(date1: Date, date2: Date): Boolean {
        return date1.year == date2.year &&
                date1.month == date2.month &&
                date1.day == date2.day
    }

    // Time
    fun timeToString(time: Time): String = timeFormat().format(time)

    private fun dateFormat() = SimpleDateFormat.getDateInstance()

    private fun timeFormat() = SimpleDateFormat("hh:mm")
}