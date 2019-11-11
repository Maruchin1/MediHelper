package com.example.medihelper

import com.example.medihelper.localdata.type.AppTime
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.*

class AppTimeTest {

    @Test
    fun appTime_NewTime_TimeInMillis() {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 30)
        }
        val time = AppTime(calendar.timeInMillis)
        with(time) {
            assertThat(hour).isEqualTo(8)
            assertThat(minute).isEqualTo(30)
        }
    }

    @Test
    fun appTime_NewTime_HourMinute() {
        val time = AppTime(8, 30)
        with(time) {
            assertThat(hour).isEqualTo(8)
            assertThat(minute).isEqualTo(30)
        }
    }

    @Test
    fun appTime_NewTime_JsonFormatString() {
        val time = AppTime("08:30")
        with(time) {
            assertThat(hour).isEqualTo(8)
            assertThat(minute).isEqualTo(30)
        }
    }

    @Test
    fun appTime_formatString() {
        val time = AppTime(8, 30)
        assertThat(time.formatString).matches("08:30")
    }

    @Test
    fun appTime_jsonFormatString() {
        val time = AppTime(8, 30)
        assertThat(time.jsonFormatString).matches("08:30")
    }

    @Test
    fun appTime_Equal() {
        val firstTime = AppTime(8, 30)
        val secondTime = AppTime(8, 30)
        assertThat(firstTime == secondTime).isTrue()
    }

    @Test
    fun appTime_NotEqual() {
        val firstTime = AppTime(8, 30)
        val secondTime = AppTime(9, 10)
        assertThat(firstTime != secondTime).isTrue()
    }

    @Test
    fun appTime_FirstBiggerHour() {
        val firstTime = AppTime(9, 0)
        val secondTime = AppTime(8, 0)
        assertThat(firstTime > secondTime).isTrue()
    }

    @Test
    fun appTime_SecondBiggerHour() {
        val firstTime = AppTime(8, 0)
        val secondTime = AppTime(9, 0)
        assertThat(secondTime > firstTime).isTrue()
    }

    @Test
    fun appTime_FirstBiggerMinute() {
        val firstTime = AppTime(8, 1)
        val secondTime = AppTime(8, 0)
        assertThat(firstTime > secondTime).isTrue()
    }

    @Test
    fun appTime_SecondBiggerMinute() {
        val firstTime = AppTime(8, 0)
        val secondTime = AppTime(8, 1)
        assertThat(secondTime > firstTime).isTrue()
    }
}