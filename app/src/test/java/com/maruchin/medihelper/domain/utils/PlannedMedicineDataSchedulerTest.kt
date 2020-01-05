package com.maruchin.medihelper.domain.utils

import com.google.common.truth.Truth
import com.maruchin.medihelper.domain.entities.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class PlannedMedicineDataSchedulerTest {

    private lateinit var scheduler: PlannedMedicineScheduler

    @Before
    fun before() {
        scheduler = PlannedMedicineScheduler()
    }

    @Test
    fun getPlannedMedicines_Once() {
        val medicinePlan = Plan(
            entityId = "xyz",
            profileId = "",
            medicineId = "",
            planType = Plan.Type.ONE_DAY,
            startDate = AppDate(2019, 11, 20),
            endDate = null,
            intakeDays = null,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                ),
                TimeDose(
                    time = AppTime(20, 0),
                    doseSize = 4f
                )
            )
        )

        val result = runBlocking {
            scheduler.getPlannedMedicines(medicinePlan)
        }

        Truth.assertThat(result.size).isEqualTo(2)
        val firstExpectedResult = PlannedMedicine(
            entityId = "",
            medicinePlanId = "xyz",
            profileId = "",
            medicineId = "",
            plannedDate = AppDate(2019, 11, 20),
            plannedTime = AppTime(9, 30),
            plannedDoseSize = 2f,
            status = PlannedMedicine.Status.NOT_TAKEN
        )
        val secondExpectedResult = PlannedMedicine(
            entityId = "",
            medicinePlanId = "xyz",
            profileId = "",
            medicineId = "",
            plannedDate = AppDate(2019, 11, 20),
            plannedTime = AppTime(20, 0),
            plannedDoseSize = 4f,
            status = PlannedMedicine.Status.NOT_TAKEN
        )
        Truth.assertThat(result[0]).isEqualTo(firstExpectedResult)
        Truth.assertThat(result[1]).isEqualTo(secondExpectedResult)
    }

    @Test
    fun getPlannedMedicines_Period_Everyday() {
        val medicinePlan = Plan(
            entityId = "xyz",
            profileId = "",
            medicineId = "",
            planType = Plan.Type.PERIOD,
            startDate = AppDate(2019, 11, 20),
            endDate = AppDate(2019, 11, 27),
            intakeDays = IntakeDays.Everyday,
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val result = runBlocking {
            scheduler.getPlannedMedicines(medicinePlan)
        }

        val firstExpectedResult = PlannedMedicine(
            entityId = "",
            medicinePlanId = "xyz",
            profileId = "",
            medicineId = "",
            plannedDate = AppDate(2019, 11, 20),
            plannedTime = AppTime(9, 30),
            plannedDoseSize = 2f,
            status = PlannedMedicine.Status.NOT_TAKEN
        )

        Truth.assertThat(result.size).isEqualTo(8)
        Truth.assertThat(result).containsExactly(
            firstExpectedResult,
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 21)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 22)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 23)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 24)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 25)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 26)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 27))
        )
    }

    @Test
    fun getPlannedMedicines_Period_DaysOfWeek() {
        val medicinePlan = Plan(
            entityId = "xyz",
            profileId = "",
            medicineId = "",
            planType = Plan.Type.PERIOD,
            startDate = AppDate(2019, 11, 20),
            endDate = AppDate(2019, 11, 27),
            intakeDays = IntakeDays.DaysOfWeek(
                monday = false,
                tuesday = true,
                wednesday = false,
                thursday = true,
                friday = false,
                saturday = true,
                sunday = false
            ),
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val result = runBlocking {
            scheduler.getPlannedMedicines(medicinePlan)
        }

        val firstExpectedResult = PlannedMedicine(
            entityId = "",
            medicinePlanId = "xyz",
            profileId = "",
            medicineId = "",
            plannedDate = AppDate(2019, 11, 21),
            plannedTime = AppTime(9, 30),
            plannedDoseSize = 2f,
            status = PlannedMedicine.Status.NOT_TAKEN
        )

        Truth.assertThat(result.size).isEqualTo(3)
        Truth.assertThat(result).containsExactly(
            firstExpectedResult,
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 23)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 26))
        )
    }

    //20, 22, 24, 26
    @Test
    fun getPlannedMedicines_Period_Interval() {
        val medicinePlan = Plan(
            entityId = "xyz",
            profileId = "",
            medicineId = "",
            planType = Plan.Type.PERIOD,
            startDate = AppDate(2019, 11, 20),
            endDate = AppDate(2019, 11, 27),
            intakeDays = IntakeDays.Interval(daysCount = 2),
            timeDoseList = listOf(
                TimeDose(
                    time = AppTime(9, 30),
                    doseSize = 2f
                )
            )
        )

        val result = runBlocking {
            scheduler.getPlannedMedicines(medicinePlan)
        }

        val firstExpectedResult = PlannedMedicine(
            entityId = "",
            medicinePlanId = "xyz",
            profileId = "",
            medicineId = "",
            plannedDate = AppDate(2019, 11, 20),
            plannedTime = AppTime(9, 30),
            plannedDoseSize = 2f,
            status = PlannedMedicine.Status.NOT_TAKEN
        )

        Truth.assertThat(result.size).isEqualTo(4)
        Truth.assertThat(result).containsExactly(
            firstExpectedResult,
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 22)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 24)),
            firstExpectedResult.copy(plannedDate = AppDate(2019, 11, 26))
        )
    }
}