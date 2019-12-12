package com.maruchin.medihelper.domain.usecases.datetime

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito

class CalcDaysDiffUseCaseTest {

    private val deviceCalendar: DeviceCalendar = mock()

    private lateinit var useCase: CalcDaysDiffUseCase

    @Before
    fun before() {
        useCase = CalcDaysDiffUseCase(deviceCalendar)
    }

    @Test
    fun execute() {
        val mockCurrDate = AppDate(2019, 12, 12)
        Mockito.`when`(deviceCalendar.getCurrTimeInMillis()).thenReturn(mockCurrDate.timeInMillis)

        val date = AppDate(2020, 1, 1)

        val result = runBlocking {
            useCase.execute(date)
        }

        Truth.assertThat(result).isEqualTo(21)
    }
}