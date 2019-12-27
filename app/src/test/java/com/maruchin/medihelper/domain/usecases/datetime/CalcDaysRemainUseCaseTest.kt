package com.maruchin.medihelper.domain.usecases.datetime

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.testingframework.mock
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Before
import org.mockito.Mockito

class CalcDaysRemainUseCaseTest {

    private val deviceCalendar: DeviceCalendar = mock()

    private lateinit var useCase: CalcDaysRemainUseCase

    @Before
    fun before() {
        useCase = CalcDaysRemainUseCase(deviceCalendar)
    }

    @Test
    fun execute() {
        val mockCurrDate = AppDate(2019, 12, 12)
        Mockito.`when`(deviceCalendar.getCurrTimeInMillis()).thenReturn(mockCurrDate.timeInMillis)

        val expireDate = AppExpireDate(2020, 1)

        val result = runBlocking {
            useCase.execute(expireDate)
        }

        Truth.assertThat(result).isEqualTo(20)
    }
}