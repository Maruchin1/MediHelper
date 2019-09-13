package com.example.medihelper

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

object AppDateTime {

    // Date
    fun getCurrCalendar() = Calendar.getInstance(TimeZone.getDefault()).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    @JvmStatic
    fun dateToString(date: Date?): String? = date?.let { dateFormat().format(it) }

    @JvmStatic
    fun dayMonthString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        return dateFormat.format(date)
    }

    @JvmStatic
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
    fun getCurrTime(): Time {
        val currCalendar = Calendar.getInstance(TimeZone.getDefault())
        val currHour = currCalendar.get(Calendar.HOUR_OF_DAY)
        val currMinute = currCalendar.get(Calendar.MINUTE)
        return Time(currHour, currMinute, 0)
    }

    @JvmStatic
    fun timeToString(time: Time?): String? = time?.let { timeFormat().format(it) }

    fun compareTimes(time1: Time, time2: Time): Int {
        val hour1 = time1.hours
        val minute1 = time1.minutes

        val hour2 = time2.hours
        val minute2 = time2.minutes

        return when {
            hour1 > hour2 -> 1
            hour2 > hour1 -> 2
            minute1 > minute2 -> 1
            minute2 > minute1 -> 2
            else -> 0
        }
    }

    private fun dateFormat() = SimpleDateFormat.getDateInstance()

    private fun timeFormat() = SimpleDateFormat("HH:mm")
}