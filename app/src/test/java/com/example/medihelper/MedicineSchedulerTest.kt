package com.example.medihelper

import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.DaysType
import com.example.medihelper.domain.entities.DurationType
import com.example.medihelper.data.local.model.PlannedMedicineEntity
import com.example.medihelper.localdata.pojo.MedicinePlanEditData
import com.example.medihelper.localdata.pojo.TimeDoseEditData
import com.example.medihelper.domain.utils.MedicineScheduler
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class MedicineSchedulerTest : KoinTest {

    private val testModule = module {
        single { MedicineScheduler() }
    }

    private val medicineScheduler: MedicineScheduler by inject()

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
    fun medicineScheduler_Once_TwoTimesPerDay() {
        val plan = MedicinePlanEditData(
            medicinePlanId = 1,
            medicineId = 1,
            personId = 1,
            durationType = DurationType.ONCE,
            startDate = AppDate(2019, 11, 10),
            timeDoseList = listOf(
                TimeDoseEditData(
                    time = AppTime(10, 30),
                    doseSize = 2f
                ),
                TimeDoseEditData(
                    time = AppTime(19, 0),
                    doseSize = 1f
                )
            )
        )
        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(plan)
        val expectedResult = arrayOf(
            PlannedMedicineEntity(
                medicinePlanId = 1,
                plannedDate = AppDate(2019, 11, 10),
                plannedTime = AppTime(10, 30),
                plannedDoseSize = 2f
            ),
            PlannedMedicineEntity(
                medicinePlanId = 1,
                plannedDate = AppDate(2019, 11, 10),
                plannedTime = AppTime(19, 0),
                plannedDoseSize = 1f
            )
        )
        assertThat(plannedMedicineList).containsExactly(*expectedResult)
    }

    @Test
    fun medicineScheduler_Period_MondayWednesdaySaturday_OneTimePerDay() {
        val plan = MedicinePlanEditData(
            medicinePlanId = 1,
            medicineId = 1,
            personId = 1,
            durationType = DurationType.PERIOD,
            startDate = AppDate(2019, 11, 10),
            endDate = AppDate(2019, 11, 17),
            daysType = DaysType.DAYS_OF_WEEK,
            daysOfWeek = DaysOfWeek().apply {
                monday = true
                wednesday = true
                saturday = true
            },
            timeDoseList = listOf(
                TimeDoseEditData(
                    time = AppTime(10, 30),
                    doseSize = 2f
                )
            )
        )
        val plannedMedicineList = medicineScheduler.getPlannedMedicinesForMedicinePlan(plan)
        val expectedResult = arrayOf(
            PlannedMedicineEntity(
                medicinePlanId = 1,
                plannedDate = AppDate(2019, 11, 11),
                plannedTime = AppTime(10, 30),
                plannedDoseSize = 2f
            ),
            PlannedMedicineEntity(
                medicinePlanId = 1,
                plannedDate = AppDate(2019, 11, 13),
                plannedTime = AppTime(10, 30),
                plannedDoseSize = 2f
            ),
            PlannedMedicineEntity(
                medicinePlanId = 1,
                plannedDate = AppDate(2019, 11, 16),
                plannedTime = AppTime(10, 30),
                plannedDoseSize = 2f
            )
        )
        assertThat(plannedMedicineList).containsExactly(*expectedResult)
    }
}