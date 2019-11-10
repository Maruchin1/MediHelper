package com.example.medihelper

import com.example.medihelper.custom.AppExpireDate
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class AppExpireDateTest {

    @Test
    fun appExpireDate_NewExpireDate_YearMonth() {
        val expireDate = AppExpireDate(2020, 6)
        with(expireDate) {
            assertThat(year).isEqualTo(2020)
            assertThat(month).isEqualTo(6)
        }
    }

    @Test
    fun appExpireDate_NewExpireDate_JsonString() {
        val expireDate = AppExpireDate("01-06-2020")
        with(expireDate) {
            assertThat(year).isEqualTo(2020)
            assertThat(month).isEqualTo(6)
        }
    }

    @Test
    fun appExpireDate_JsonFormatString() {
        val expireDate = AppExpireDate(2020, 6)
        assertThat(expireDate.jsonFormatString).isEqualTo("01-06-2020")
    }

    @Test
    fun appExpireDate_Equal() {
        val firstExpireDate = AppExpireDate(2020, 6)
        val secondExpireDate = AppExpireDate(2020, 6)
        assertThat(firstExpireDate == secondExpireDate)
    }

    @Test
    fun appExpireDate_NotEqual() {
        val firstExpireDate = AppExpireDate(2020, 7)
        val secondExpireDate = AppExpireDate(2020, 6)
        assertThat(firstExpireDate != secondExpireDate)
    }

    @Test
    fun appExpireDate_FirsBiggerMonth() {
        val firstExpireDate = AppExpireDate(2020, 7)
        val secondExpireDate = AppExpireDate(2020, 6)
        assertThat(firstExpireDate > secondExpireDate)
    }

    @Test
    fun appExpireDate_SecondBiggerMonth() {
        val firstExpireDate = AppExpireDate(2020, 6)
        val secondExpireDate = AppExpireDate(2020, 7)
        assertThat(firstExpireDate < secondExpireDate)
    }
}