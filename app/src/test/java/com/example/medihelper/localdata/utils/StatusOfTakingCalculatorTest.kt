package com.example.medihelper.localdata.utils

import com.example.medihelper.localdata.type.AppDate
import com.example.medihelper.localdata.type.AppTime
import com.example.medihelper.localdata.type.StatusOfTaking
import com.google.common.truth.Truth
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class StatusOfTakingCalculatorTest : KoinTest {

    val testModule = module {
        single { StatusOfTakingCalculator() }
    }

    private val calculator: StatusOfTakingCalculator by inject()

    @Before
    fun before() {
        startKoin {
            modules(testModule)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun getByTaken_CurrTaken_ReturnWaiting() {
        val plannedDate = AppDate(2019, 11, 13)
        val plannedTime = AppTime(8, 0)
        val currDate = AppDate(2019, 11, 13)
        val currTime = AppTime(19, 30)
        val taken = false

        val resultStatus = calculator.getByTaken(plannedDate, plannedTime, currDate, currTime, taken)

        Truth.assertThat(resultStatus).isEqualTo(StatusOfTaking.NOT_TAKEN)
    }



}