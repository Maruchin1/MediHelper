package com.example.medihelper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class AppDateTest {

    @Test
    fun appDate_constructor_timeInMillis() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.MONTH, 10)
            set(Calendar.YEAR, 2019)
        }
        val calendarTimeInMillis = calendar.timeInMillis
        val appDate = AppDate(calendarTimeInMillis)
        assertThat(appDate.year).isEqualTo(2019)
        assertThat(appDate.month).isEqualTo(10)
        assertThat(appDate.day).isEqualTo(1)
        assertThat(appDate.dayOfWeek).isEqualTo(6)
    }

    @Test
    fun appDate_constructor_jsonFormatString() {
        val date = AppDate("01-11-2019")
        assertThat(date == AppDate(2019, 10, 1))
    }

    @Test
    fun appDate_monthYearString() {
        val date = AppDate(2019, 10, 1)
        assertThat(date.monthYearString).matches("11.2019")
    }

    @Test
    fun appDate_jsonFormatSting() {
        val date = AppDate(2019, 10, 1)
        assertThat(date.jsonFormatString).matches("01-11-2019")
    }

    @Test
    fun appDate_addDays() {
        val date = AppDate(2019, 10, 1)
        date.addDays(12)
        assertThat(date.year).isEqualTo(2019)
        assertThat(date.month).isEqualTo(10)
        assertThat(date.day).isEqualTo(13)
    }

    @Test
    fun appDate_copy() {
        val date = AppDate(2019, 10, 1)
        val dateCopy = date.copy()
        dateCopy.addDays(5)
        assertThat(date == dateCopy) .isFalse()
    }

    @Test
    fun appDate_equals() {
        val firstDate = AppDate(2019, 10, 1)
        val secondDate = AppDate(2019, 10, 1)
        assertThat(firstDate == secondDate).isTrue()
    }

    @Test
    fun appDate_notEquals() {
        val firstDate = AppDate(2019, 10, 1)
        val secondDate = AppDate(2019, 10, 2)
        assertThat(firstDate != secondDate).isTrue()
    }

    @Test
    fun appDate_compare_secondBiggerDay() {
        val firstDate = AppDate(2019, 10, 1)
        val secondDate = AppDate(2019, 10, 2)
        assertThat(secondDate > firstDate).isTrue()
    }

    @Test
    fun appDate_compare_firstBiggerDay() {
        val firstDate = AppDate(2019, 10, 2)
        val secondDate = AppDate(2019, 10, 1)
        assertThat(firstDate > secondDate).isTrue()
    }
}