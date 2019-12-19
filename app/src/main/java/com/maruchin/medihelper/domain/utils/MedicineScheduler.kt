package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.ArrayList

class MedicineScheduler {

    suspend fun getCalendarEntries(medicinePlan: MedicinePlan): List<MedicineCalendarEntry> =
        withContext(Dispatchers.Default) {
            return@withContext when (medicinePlan.planType) {
                MedicinePlan.Type.ONCE -> getForDate(
                    medicinePlanId = medicinePlan.medicinePlanId,
                    plannedDate = medicinePlan.startDate,
                    timeDoseList = medicinePlan.timeDoseList
                )
                MedicinePlan.Type.PERIOD -> when (medicinePlan.intakeDays) {
                    is IntakeDays.Everyday -> getForEveryday(medicinePlan)
                    is IntakeDays.DaysOfWeek -> getForDaysOfWeek(medicinePlan)
                    is IntakeDays.Interval -> getForInterval(medicinePlan)
                    is IntakeDays.Sequence -> getForSequence(medicinePlan)
                    else -> emptyList()
                }
                MedicinePlan.Type.CONTINUOUS -> {
                    val tempMedicinePlan = medicinePlan.copy(
                        endDate = medicinePlan.startDate.copy().apply {
                            addDays(CONTINUOUS_DAYS_COUNT)
                        }
                    )
                    when (medicinePlan.intakeDays) {
                        is IntakeDays.Everyday -> getForEveryday(tempMedicinePlan)
                        is IntakeDays.DaysOfWeek -> getForDaysOfWeek(tempMedicinePlan)
                        is IntakeDays.Interval -> getForInterval(tempMedicinePlan)
                        is IntakeDays.Sequence -> getForSequence(tempMedicinePlan)
                        else -> emptyList()
                    }
                }
            }
        }

    private fun getForEveryday(medicinePlan: MedicinePlan): List<MedicineCalendarEntry> {
        val entriesList = mutableListOf<MedicineCalendarEntry>()
        val currDate = medicinePlan.startDate.copy()

        while (currDate <= medicinePlan.endDate!!) {
            entriesList.addAll(
                getForDate(
                    medicinePlanId = medicinePlan.medicinePlanId,
                    plannedDate = currDate.copy(),
                    timeDoseList = medicinePlan.timeDoseList
                )
            )
            currDate.addDays(1)
        }
        return entriesList
    }

    private fun getForDaysOfWeek(medicinePlan: MedicinePlan): List<MedicineCalendarEntry> {
        val entriesList = mutableListOf<MedicineCalendarEntry>()
        val currDate = medicinePlan.startDate.copy()

        while (currDate <= medicinePlan.endDate!!) {
            val daysOfWeek = medicinePlan.intakeDays as IntakeDays.DaysOfWeek
            if (daysOfWeek.isDaySelected(currDate.dayOfWeek)) {
                entriesList.addAll(
                    getForDate(
                        medicinePlanId = medicinePlan.medicinePlanId,
                        plannedDate = currDate.copy(),
                        timeDoseList = medicinePlan.timeDoseList
                    )
                )
            }
            currDate.addDays(1)
        }
        return entriesList
    }

    private fun getForInterval(medicinePlan: MedicinePlan): List<MedicineCalendarEntry> {
        val entriesList = mutableListOf<MedicineCalendarEntry>()
        val currDate = medicinePlan.startDate.copy()

        while (currDate <= medicinePlan.endDate!!) {
            entriesList.addAll(
                getForDate(
                    medicinePlanId = medicinePlan.medicinePlanId,
                    plannedDate = currDate.copy(),
                    timeDoseList = medicinePlan.timeDoseList
                )
            )
            val interval = medicinePlan.intakeDays as IntakeDays.Interval
            currDate.addDays(interval.daysCount)
        }
        return entriesList
    }

    private fun getForSequence(medicinePlan: MedicinePlan): List<MedicineCalendarEntry> {
        val entriesList = mutableListOf<MedicineCalendarEntry>()
        //todo dopisać logikę dla sekwencji
        return entriesList
    }

    private fun getForDate(
        medicinePlanId: String,
        plannedDate: AppDate,
        timeDoseList: List<TimeDose>
    ): List<MedicineCalendarEntry> {
        val entriesList = mutableListOf<MedicineCalendarEntry>()
        timeDoseList.forEach { timeDose ->
            entriesList.add(
                MedicineCalendarEntry(
                    medicineCalendarEntryId = "",
                    medicinePlanId = medicinePlanId,
                    plannedDate = plannedDate,
                    plannedTime = timeDose.time,
                    plannedDoseSize = timeDose.doseSize,
                    status = MedicineCalendarEntry.Status.WAITING
                )
            )
        }
        return entriesList
    }

    companion object {
        private const val CONTINUOUS_DAYS_COUNT = 60
    }
}