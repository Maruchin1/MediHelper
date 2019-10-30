package com.example.medihelper

import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DaysOfWeekTest {

    @Test
    fun daysOfWeek_IsDaySelected_True() {
        val daysOfWeek = MedicinePlanEntity.DaysOfWeek().apply {
            thursday = true
        }
        assertThat(daysOfWeek.isDaySelected(5)).isTrue()
    }

    @Test
    fun daysOfWeek_IsDaySelected_False() {
        val daysOfWeek = MedicinePlanEntity.DaysOfWeek().apply {
            thursday = true
        }
        assertThat(daysOfWeek.isDaySelected(4)).isFalse()
    }

    @Test
    fun daysOfWeek_GetSelectedDaysString() {
        val daysOfWeek = MedicinePlanEntity.DaysOfWeek().apply {
            monday = true
            wednesday = true
            saturday = true
        }
        assertThat(daysOfWeek.getSelectedDaysString()).matches("poniedziałek, środa, sobota")
    }

    @Test
    fun daysOfWeek_ToString() {
        val daysOfWeek = MedicinePlanEntity.DaysOfWeek().apply {
            monday = true
            wednesday = true
            saturday = true
        }
        assertThat(daysOfWeek.toString())
            .matches("monday:true, tuesday:false, wednesday:true, thursday:false, friday:false, saturday:true, sunday:false")
    }
}