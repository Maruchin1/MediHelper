package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppExpireDate
import org.junit.Before
import org.junit.Test

class DateTimeCalculatorTest {

    private lateinit var calculator: DateTimeCalculator

    @Before
    fun before() {
        calculator = DateTimeCalculator()
    }

    @Test
    fun calcDaysDiff() {
        val startDate = AppDate(2019, 11, 1)
        val endDate = AppExpireDate(2019, 12)

        val result = calculator.calcDaysDiff(startDate, endDate)

        Truth.assertThat(result).isEqualTo(30)
    }
}