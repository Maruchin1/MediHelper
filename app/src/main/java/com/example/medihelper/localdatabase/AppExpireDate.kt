package com.example.medihelper.localdatabase

import java.text.SimpleDateFormat
import java.util.*

class AppExpireDate : Comparable<AppExpireDate> {

    val year: Int
        get() = dateCalendar.get(Calendar.YEAR)
    val month: Int
        get() = dateCalendar.get(Calendar.MONTH) + 1

    val formatString: String
        get() = SimpleDateFormat("MM.yyyy", Locale.getDefault()).format(dateCalendar.time)
    val jsonFormatString: String
        get() = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dateCalendar.time)

    private val dateCalendar: Calendar

    constructor(year: Int, month: Int) {
        dateCalendar = Calendar.getInstance().apply {
            set(year, month - 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    constructor(jsonFormatString: String) {
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(jsonFormatString)
        dateCalendar = Calendar.getInstance().apply {
            this.time = date
            listOf(
                Calendar.HOUR_OF_DAY,
                Calendar.MINUTE,
                Calendar.SECOND,
                Calendar.MILLISECOND
            ).forEach { calendarField ->
                set(calendarField, 0)
            }
            set(Calendar.DATE, 1)
        }
    }

    override fun compareTo(other: AppExpireDate): Int {
        return when {
            year > other.year -> 1
            year < other.year -> -1
            month > other.month -> 1
            month < other.month -> -1
            else -> 0
        }
    }

    override fun toString(): String {
        return formatString
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppExpireDate

        if (dateCalendar != other.dateCalendar) return false

        return true
    }

    override fun hashCode(): Int {
        return dateCalendar.hashCode()
    }
}