package com.maruchin.medihelper.domain.entities

sealed class IntakeDays {

    object Everyday : IntakeDays()

    data class DaysOfWeek(
        val monday: Boolean,
        val tuesday: Boolean,
        val wednesday: Boolean,
        val thursday: Boolean,
        val friday: Boolean,
        val saturday: Boolean,
        val sunday: Boolean
    ) : IntakeDays()

    data class Interval(val daysCount: Int) : IntakeDays()

    data class Sequence(val intakeCount: Int, val notIntakeCount: Int) : IntakeDays()
}