package com.maruchin.medihelper.domain.entities

import com.google.common.truth.Truth
import org.junit.Test

class IntakeDaysTest {

    @Test
    fun isDaySelected_1_Sunday_True() {
        val daysOfWeek = IntakeDays.DaysOfWeek(
            monday = false,
            tuesday = false,
            wednesday = false,
            thursday = false,
            friday = false,
            saturday = false,
            sunday = true
        )

        val result = daysOfWeek.isDaySelected(1)

        Truth.assertThat(result).isTrue()
    }

    @Test
    fun isDaySelected_1_Sunday_False() {
        val daysOfWeek = IntakeDays.DaysOfWeek(
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = true,
            sunday = false
        )

        val result = daysOfWeek.isDaySelected(1)

        Truth.assertThat(result).isFalse()
    }

    @Test
    fun isDaySelected_7_Saturday_True() {
        val daysOfWeek = IntakeDays.DaysOfWeek(
            monday = false,
            tuesday = false,
            wednesday = false,
            thursday = false,
            friday = false,
            saturday = true,
            sunday = false
        )

        val result = daysOfWeek.isDaySelected(7)

        Truth.assertThat(result).isTrue()
    }

    @Test
    fun isDaySelected_7_Saturday_False() {
        val daysOfWeek = IntakeDays.DaysOfWeek(
            monday = true,
            tuesday = true,
            wednesday = true,
            thursday = true,
            friday = true,
            saturday = false,
            sunday = true
        )

        val result = daysOfWeek.isDaySelected(7)

        Truth.assertThat(result).isFalse()
    }
}