package com.example.medihelper

import com.example.medihelper.localdata.type.AppDate
import org.junit.Test
import java.util.*
import com.google.common.truth.Truth.assertThat

class AppDateTest {

    @Test
    fun appDate_NewDate_TimeInMillis() {
        val calendar = Calendar.getInstance().apply {
            set(2019, 10, 13)
        }
        val date = AppDate(calendar.timeInMillis)
        with(date) {
            assertThat(year).isEqualTo(2019)
            assertThat(month).isEqualTo(11)
            assertThat(day).isEqualTo(13)
            assertThat(dayOfWeek).isEqualTo(4)
        }
    }

    @Test
    fun appDate_NewDate_YearMonthDay() {
        val date = AppDate(2019, 10, 1)
        with(date) {
            assertThat(year).isEqualTo(2019)
            assertThat(month).isEqualTo(10)
            assertThat(day).isEqualTo(1)
            assertThat(dayOfWeek).isEqualTo(3)
        }
    }

    @Test
    fun appDate_NewDate_JsonString() {
        val date = AppDate("01-10-2019")
        with(date) {
            assertThat(year).isEqualTo(2019)
            assertThat(month).isEqualTo(10)
            assertThat(day).isEqualTo(1)
            assertThat(dayOfWeek).isEqualTo(3)
        }
    }

    @Test
    fun appDate_JsonFormatString() {
        val date = AppDate(2019, 10, 1)
        assertThat(date.jsonFormatString).isEqualTo("01-10-2019")
    }

    @Test
    fun appDate_Copy() {
        val date = AppDate(2019, 10, 1)
        val dateCopy = date.copy()
        assertThat(dateCopy).isNotSameInstanceAs(date)
    }

    @Test
    fun appDate_AddDays() {
        val date = AppDate(2019, 10, 1)
        date.addDays(12)
        assertThat(date.year).isEqualTo(2019)
        assertThat(date.month).isEqualTo(10)
        assertThat(date.day).isEqualTo(13)
    }

    @Test
    fun appDate_equals() {
        val firstDate = AppDate(2019, 10, 1)
        val secondDate = AppDate(2019, 10, 1)
        assertThat(firstDate == secondDate)
    }

    @Test
    fun appDate_notEquals() {
        val firstDate = AppDate(2019, 10, 1)
        val secondDate = AppDate(2019, 10, 2)
        assertThat(firstDate != secondDate)
    }

    @Test
    fun appDate_compare_secondBiggerDay() {
        val firstDate = AppDate(2019, 10, 1)
        val secondDate = AppDate(2019, 10, 2)
        assertThat(secondDate > firstDate)
    }

    @Test
    fun appDate_compare_firstBiggerDay() {
        val firstDate = AppDate(2019, 10, 2)
        val secondDate = AppDate(2019, 10, 1)
        assertThat(firstDate > secondDate)
    }
}