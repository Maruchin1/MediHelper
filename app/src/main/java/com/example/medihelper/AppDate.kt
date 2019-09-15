package com.example.medihelper

import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*


class AppDate : Date {

    constructor(timeInMillis: Long) : super(
        Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    )

    constructor(year: Int, month: Int, day: Int) : super(
        Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    )

    val formatString: String
        get() = SimpleDateFormat.getDateInstance().format(this)

    val dayMonthString: String
        get() = SimpleDateFormat("dd MMM", Locale.getDefault()).format(this)

    val dayOfWeekString: String
        get() = SimpleDateFormat("EEE", Locale.getDefault()).format(this)

    companion object {

        fun currDate(): AppDate {
            val currCalendar = Calendar.getInstance()
            return AppDate(currCalendar.timeInMillis)
        }

        fun daysBetween(date1: Date, date2: Date): Long {
            val days1 = date1.time / (24 * 3600 * 1000)
            val days2 = date2.time / (24 * 3600 * 1000)
            val daysDiff = days2 - days1
            return daysDiff
        }

        fun compareDates(date1: Date, date2: Date): Int {
            val calendar1 = Calendar.getInstance().apply { time = date1 }
            val calendar2 = Calendar.getInstance().apply { time = date2 }

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
    }
}