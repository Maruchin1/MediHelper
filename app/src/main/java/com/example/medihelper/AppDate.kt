package com.example.medihelper

import java.text.SimpleDateFormat
import java.util.*


class AppDate {

    val timeInMillis: Long
        get() = dateCalendar.timeInMillis
    val year: Int
        get() = dateCalendar.get(Calendar.YEAR)
    val month: Int
        get() = dateCalendar.get(Calendar.MONTH)
    val day: Int
        get() = dateCalendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek: Int
        get() = dateCalendar.get(Calendar.DAY_OF_WEEK)

    val formatString: String
        get() = SimpleDateFormat.getDateInstance().format(dateCalendar.time)
    val dayMonthString: String
        get() = SimpleDateFormat("dd MMM", Locale.getDefault()).format(dateCalendar.time)
    val dayOfWeekString: String
        get() = SimpleDateFormat("EEE", Locale.getDefault()).format(dateCalendar.time)

    private val dateCalendar: Calendar

    constructor(timeInMillis: Long) {
        dateCalendar = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            listOf(Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND).forEach { calendarField ->
                set(calendarField, 0)
            }
        }
    }

    constructor(year: Int, month: Int, day: Int) {
        dateCalendar = Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    fun addDays(daysCount: Int) = dateCalendar.add(Calendar.DATE, daysCount)

    fun copy() = AppDate(timeInMillis)

    companion object {
        fun currDate() = AppDate(Calendar.getInstance().timeInMillis)

        fun daysBetween(date1: AppDate, date2: AppDate): Long {
            val days1 = date1.timeInMillis / (24 * 3600 * 1000)
            val days2 = date2.timeInMillis / (24 * 3600 * 1000)
            val daysDiff = days2 - days1
            return daysDiff
        }

        fun compareDates(date1: AppDate, date2: AppDate) = when {
            date1.year > date2.year -> 1
            date1.year < date2.year -> 2
            date1.month > date2.month -> 1
            date1.month < date2.month -> 2
            date1.day > date2.day -> 1
            date1.day < date2.day -> 2
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppDate

        if (dateCalendar != other.dateCalendar) return false

        return true
    }

    override fun hashCode(): Int {
        return dateCalendar.hashCode()
    }
}