package com.example.medihelper

import java.sql.Time
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

object AppDateTimeUtil {

    // Date
    fun getCurrCalendar(): Calendar {
        return Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

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

    fun makeDate(time: Long): Date {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }

    fun makeDate(day: Int, month: Int, year: Int): Date {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.time
    }

    fun daysBetween(date1: Date, date2: Date): Long {
        val days1 = date1.time / (24 * 3600 * 1000)
        val days2 = date2.time / (24 * 3600 * 1000)
        val daysDiff = days2 - days1
        return daysDiff
    }

    fun compareDates(date1: Date, date2: Date): Int {
        val calendar1 = Calendar.getInstance().apply {
            time = date1
        }
        val calendar2 = Calendar.getInstance().apply {
            time = date2
        }
        val year1 = calendar1.get(Calendar.YEAR)
        val month1 = calendar1.get(Calendar.MONTH)
        val day1 = calendar1.get(Calendar.DAY_OF_MONTH)

        val year2 = calendar2.get(Calendar.YEAR)
        val month2 = calendar2.get(Calendar.MONTH)
        val day2 = calendar2.get(Calendar.DAY_OF_MONTH)

        return when {
            year1 > year2 -> 1
            year2 > year1 -> 2
            month1 > month2 -> 1
            month2 > month1 -> 2
            day1 > day2 -> 1
            day2 > day1 -> 2
            else -> 0
        }
    }

    // Time
    fun timeToString(time: Time): String = timeFormat().format(time)

    private fun dateFormat() = SimpleDateFormat.getDateInstance()

    private fun timeFormat() = SimpleDateFormat("hh:mm")
}