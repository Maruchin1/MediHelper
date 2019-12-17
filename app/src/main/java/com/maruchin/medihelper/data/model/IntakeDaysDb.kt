package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.IntakeDays

data class IntakeDaysDb(
    val monday: Boolean? = null,
    val tuesday: Boolean? = null,
    val wednesday: Boolean? = null,
    val thursday: Boolean? = null,
    val friday: Boolean? = null,
    val saturday: Boolean? = null,
    val sunday: Boolean? = null,
    val daysCount: Int? = null,
    val intakeCount: Int? = null,
    val notIntakeCount: Int? = null
) {
    constructor(entity: IntakeDays) : this(
        monday = if (entity is IntakeDays.DaysOfWeek) entity.monday else null,
        tuesday = if (entity is IntakeDays.DaysOfWeek) entity.tuesday else null,
        wednesday = if (entity is IntakeDays.DaysOfWeek) entity.wednesday else null,
        thursday = if (entity is IntakeDays.DaysOfWeek) entity.thursday else null,
        friday = if (entity is IntakeDays.DaysOfWeek) entity.friday else null,
        saturday = if (entity is IntakeDays.DaysOfWeek) entity.saturday else null,
        sunday = if (entity is IntakeDays.DaysOfWeek) entity.sunday else null,
        daysCount = if (entity is IntakeDays.Interval) entity.daysCount else null,
        intakeCount = if (entity is IntakeDays.Sequence) entity.intakeCount else null,
        notIntakeCount = if (entity is IntakeDays.Sequence) entity.notIntakeCount else null
    )

    fun toEntity(): IntakeDays {
        if (arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday).all { it != null }) {
            return IntakeDays.DaysOfWeek(
                monday!!, tuesday!!, wednesday!!, thursday!!, friday!!, saturday!!, sunday!!
            )
        } else if (daysCount != null) {
            return IntakeDays.Interval(daysCount)
        } else if (intakeCount != null && notIntakeCount != null) {
            return IntakeDays.Sequence(intakeCount, notIntakeCount)
        } else {
            return IntakeDays.Everyday
        }
    }
}