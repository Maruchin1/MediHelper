package com.maruchin.medihelper.domain.entities

import java.io.Serializable


sealed class IntakeDays{

    object Everyday : IntakeDays(), Serializable

    data class DaysOfWeek(
        var monday: Boolean,
        var tuesday: Boolean,
        var wednesday: Boolean,
        var thursday: Boolean,
        var friday: Boolean,
        var saturday: Boolean,
        var sunday: Boolean
    ) : IntakeDays(), Serializable {

        fun isDaySelected(dayOfWeek: Int): Boolean {
            val daysMap = mapOf(
                2 to monday,
                3 to tuesday,
                4 to wednesday,
                5 to thursday,
                6 to friday,
                7 to saturday,
                1 to sunday
            )
            return daysMap[dayOfWeek] ?: throw Exception("Incorrect day number $dayOfWeek. Value must me from 1 to 7")
        }
    }

    data class Interval(var daysCount: Int) : IntakeDays(), Serializable

    data class Sequence(var intakeCount: Int, var notIntakeCount: Int) : IntakeDays(), Serializable
}