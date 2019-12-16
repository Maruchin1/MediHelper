package com.maruchin.medihelper.domain.entities

sealed class IntakeDays {

    object Everyday : IntakeDays()

    data class DaysOfWeek(
        var monday: Boolean,
        var tuesday: Boolean,
        var wednesday: Boolean,
        var thursday: Boolean,
        var friday: Boolean,
        var saturday: Boolean,
        var sunday: Boolean
    ) : IntakeDays()

    data class Interval(var daysCount: Int) : IntakeDays()

    data class Sequence(var intakeCount: Int, var notIntakeCount: Int) : IntakeDays()
}