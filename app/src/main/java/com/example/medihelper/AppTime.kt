package com.example.medihelper

import java.text.SimpleDateFormat
import java.util.*

class AppTime : Comparable<AppTime> {

    val timeInMillis: Long
        get() = timeCalendar.timeInMillis
    val hour: Int
        get() = timeCalendar.get(Calendar.HOUR_OF_DAY)
    val minute: Int
        get() = timeCalendar.get(Calendar.MINUTE)

    val formatString: String
        get() = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeCalendar.time)

    private val timeCalendar: Calendar

    constructor(timeInMillis: Long) {
        timeCalendar = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis
            listOf(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.SECOND, Calendar.MILLISECOND).forEach { calendarField ->
                set(calendarField, 0)
            }
        }
    }

    constructor(hour: Int, minute: Int) {
        timeCalendar = Calendar.getInstance().apply {
            set(0, 0, 0, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    companion object {
        fun currTime() = AppTime(Calendar.getInstance().timeInMillis)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppTime

        if (timeCalendar != other.timeCalendar) return false

        return true
    }

    override fun hashCode(): Int {
        return timeCalendar.hashCode()
    }

    override fun compareTo(other: AppTime): Int {
        return when {
            hour > other.hour -> 1
            hour < other.hour -> -1
            minute > other.minute -> 1
            minute < other.minute -> -1
            else -> 0
        }
    }
}