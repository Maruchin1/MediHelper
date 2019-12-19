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
            //todo dopisać logikę

            return true
        }
    }

    data class Interval(var daysCount: Int) : IntakeDays(), Serializable

    data class Sequence(var intakeCount: Int, var notIntakeCount: Int) : IntakeDays(), Serializable
}