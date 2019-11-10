package com.example.medihelper

import com.example.medihelper.custom.AppDate
import com.example.medihelper.custom.AppTime
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojo.MedicinePlanEditData
import com.example.medihelper.localdatabase.pojo.TimeDoseEditData
import com.example.medihelper.localdatabase.MedicineScheduler
import org.junit.BeforeClass
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import com.google.common.truth.Truth.assertThat

class MedicineSchedulerTest : KoinTest {

    private val medicineScheduler: MedicineScheduler by inject()

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            startKoin {
                modules(utilsModule)
            }
        }
    }

    @Test
    fun medicineScheduler_Once_TwoTimesPerDay() {
        val plan = MedicinePlanEditData(
            medicinePlanID = 1,
            medicineID = 1,
            personID = 1,
            durationType = MedicinePlanEntity.DurationType.ONCE,
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
            medicinePlanID = 1,
            medicineID = 1,
            personID = 1,
            durationType = MedicinePlanEntity.DurationType.PERIOD,
            startDate = AppDate(2019, 11, 10),
            endDate = AppDate(2019, 11, 17),
            daysType = MedicinePlanEntity.DaysType.DAYS_OF_WEEK,
            daysOfWeek = MedicinePlanEntity.DaysOfWeek().apply {
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