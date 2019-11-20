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