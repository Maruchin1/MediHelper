package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlannedMedicineScheduler {

    companion object {
        private const val CONTINUOUS_DAYS_COUNT = 30
    }

    suspend fun getPlannedMedicines(plan: Plan): List<PlannedMedicine> =
        withContext(Dispatchers.Default) {
            return@withContext when (plan.planType) {
                Plan.Type.ONE_DAY -> getForOneDay(plan)
                Plan.Type.PERIOD -> getForPeriod(plan)
                Plan.Type.CONTINUOUS -> getForContinuous(plan)
            }
        }

    private fun getForOneDay(plan: Plan): List<PlannedMedicine> {
        return getForDate(plan, plan.startDate)
    }

    private fun getForPeriod(plan: Plan): List<PlannedMedicine> {
        return when (plan.intakeDays) {
            is IntakeDays.Everyday -> getForEveryday(plan)
            is IntakeDays.DaysOfWeek -> getForDaysOfWeek(plan)
            is IntakeDays.Interval -> getForInterval(plan)
            is IntakeDays.Sequence -> getForSequence(plan)
            else -> emptyList()
        }
    }

    private fun getForContinuous(plan: Plan): List<PlannedMedicine> {
        val tempMedicinePlan = plan.copy(
            endDate = plan.startDate.copy().apply {
                addDays(CONTINUOUS_DAYS_COUNT)
            }
        )
        return when (plan.intakeDays) {
            is IntakeDays.Everyday -> getForEveryday(tempMedicinePlan)
            is IntakeDays.DaysOfWeek -> getForDaysOfWeek(tempMedicinePlan)
            is IntakeDays.Interval -> getForInterval(tempMedicinePlan)
            is IntakeDays.Sequence -> getForSequence(tempMedicinePlan)
            else -> emptyList()
        }
    }

    private fun getForEveryday(plan: Plan): List<PlannedMedicine> {
        val entriesList = mutableListOf<PlannedMedicine>()
        val currDate = plan.startDate.copy()

        while (currDate <= plan.endDate!!) {
            entriesList.addAll(
                getForDate(plan, currDate.copy())
            )
            currDate.addDays(1)
        }
        return entriesList
    }

    private fun getForDaysOfWeek(plan: Plan): List<PlannedMedicine> {
        val entriesList = mutableListOf<PlannedMedicine>()
        val currDate = plan.startDate.copy()

        while (currDate <= plan.endDate!!) {
            val daysOfWeek = plan.intakeDays as IntakeDays.DaysOfWeek
            if (daysOfWeek.isDaySelected(currDate.dayOfWeek)) {
                entriesList.addAll(
                    getForDate(plan, currDate.copy())
                )
            }
            currDate.addDays(1)
        }
        return entriesList
    }

    private fun getForInterval(plan: Plan): List<PlannedMedicine> {
        val entriesList = mutableListOf<PlannedMedicine>()
        val currDate = plan.startDate.copy()

        while (currDate <= plan.endDate!!) {
            entriesList.addAll(
                getForDate(plan, currDate.copy())
            )
            val interval = plan.intakeDays as IntakeDays.Interval
            currDate.addDays(interval.daysCount)
        }
        return entriesList
    }

    private fun getForSequence(plan: Plan): List<PlannedMedicine> {
        val entriesList = mutableListOf<PlannedMedicine>()
        val currDate = plan.startDate.copy()

        val intakeDays = plan.intakeDays as IntakeDays.Sequence
        var intakeCountIterator = 1


        while (currDate <= plan.endDate!!) {
            if (intakeCountIterator > intakeDays.intakeCount) {
                currDate.addDays(intakeDays.notIntakeCount)
                intakeCountIterator = 1
            } else {
                entriesList.addAll(
                    getForDate(plan, currDate.copy())
                )
                currDate.addDays(1)
                intakeCountIterator++
            }
        }
        return entriesList
    }

    private fun getForDate(
        plan: Plan,
        plannedDate: AppDate
    ): List<PlannedMedicine> {
        val entriesList = mutableListOf<PlannedMedicine>()
        plan.timeDoseList.forEach { timeDose ->
            entriesList.add(
                PlannedMedicine(
                    medicinePlanId = plan.entityId,
                    profileId = plan.profileId,
                    medicineId = plan.medicineId,
                    plannedDate = plannedDate,
                    plannedTime = timeDose.time,
                    plannedDoseSize = timeDose.doseSize,
                    status = PlannedMedicine.Status.NOT_TAKEN
                )
            )
        }
        return entriesList
    }
}