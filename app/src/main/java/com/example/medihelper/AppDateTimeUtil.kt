package com.example.medihelper

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

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
        return ((date2.time - date1.time) / (1000 * 60 * 60 * 24)).toInt() + 1
    }

    // Time
    fun timeToString(time: Time): String = timeFormat().format(time)

    private fun dateFormat() = SimpleDateFormat.getDateInstance()

    private fun timeFormat() = SimpleDateFormat("hh:mm")
}