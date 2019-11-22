package com.example.medihelper.domain.entities

data class DaysOfWeek(
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
) {
    fun isDaySelected(numberOfDay: Int): Boolean {
        return when (numberOfDay) {
            1 -> sunday
            2 -> monday
            3 -> tuesday
            4 -> wednesday
            5 -> thursday
            6 -> friday
            7 -> saturday
            else -> throw Exception("Incorrect number of day")
        }
    }

    val selectedDaysString: String
        get() = StringBuilder().run {
            if (monday) {
                append("poniedziałek, ")
            }
            if (tuesday) {
                append("wtorek, ")
            }
            if (wednesday) {
                append("środa, ")
            }
            if (thursday) {
                append("czwartek, ")
            }
            if (friday) {
                append("piątek, ")
            }
            if (saturday) {
                append("sobota, ")
            }
            if (sunday) {
                append("niedziela, ")
            }
            setLength(length - 2)
            toString()
        }
}