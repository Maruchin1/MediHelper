package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTime
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import java.util.*

class ScheduleViewModel : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    fun getDateForPosition(position: Int): Date {
        val calendar = AppDateTime.getCurrCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position - (timelineDaysCount / 2))
        return calendar.time
    }

    fun getMedicinePlaListLive() = AppRepository.getMedicinePlanItemListLive()

    fun getPlannedMedicinesByDateListLive(date: Date) = AppRepository.getPlannedMedicineItemListLiveByDate(date)

    fun getMedicinePlanDisplayData(medicinePlanItem: MedicinePlanItem): MedicinePlanDisplayData {
        return MedicinePlanDisplayData(
            medicinePlanID = medicinePlanItem.medicinePlanID,
            medicineName = medicinePlanItem.medicineName,
            durationType = when (medicinePlanItem.durationType) {
                MedicinePlanEntity.DurationType.ONCE -> "Jednorazowo"
                MedicinePlanEntity.DurationType.PERIOD -> "Przez ${AppDateTime.daysBetween(
                    medicinePlanItem.startDate,
                    medicinePlanItem.endDate!!
                )} dni"
                MedicinePlanEntity.DurationType.CONTINUOUS -> "Leczenie ciągłe"
            },
            startDate = when (medicinePlanItem.durationType) {
                MedicinePlanEntity.DurationType.ONCE -> AppDateTime.dateToString(medicinePlanItem.startDate)
                else -> "Od ${AppDateTime.dateToString(medicinePlanItem.startDate)}"
            },
            endDate = when (medicinePlanItem.durationType) {
                MedicinePlanEntity.DurationType.PERIOD -> "Do ${AppDateTime.dateToString(medicinePlanItem.endDate!!)}"
                else -> ""
            },
            daysType = when (medicinePlanItem.daysType) {
                MedicinePlanEntity.DaysType.NONE -> ""
                MedicinePlanEntity.DaysType.EVERYDAY -> "Codziennie"
                MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> medicinePlanItem.daysOfWeek?.getSelectedDaysString() ?: "--"
                MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> "Co ${medicinePlanItem.intervalOfDays ?: "--"} dni"
            },
            timeOfTaking = StringBuilder().run {
                medicinePlanItem.timeOfTakingList.forEach { timeOfTaking ->
                    append(AppDateTime.timeToString(timeOfTaking.time))
                    append(" - ")
                    append(timeOfTaking.doseSize)
                    append(" ")
                    append(medicinePlanItem.medicineUnit)
                    append("\n")
                }
                toString()
            }
        )
    }

    fun deleteMedicinePlan(medicinePlanID: Int) = AppRepository.deleteMedicinePlan(medicinePlanID)

//    data class PlannedMedicineDisplayData(
//        val plannedMedicineEntityRef: PlannedMedicineEntity,
//        val medicineName: String,
//        val doseSize: String,
//        val time: String,
//        val statusOfTaking: String,
//        val statusOfTakingColorId: Int
//    )

    data class MedicinePlanDisplayData(
        val medicinePlanID: Int,
        val medicineName: String,
        val durationType: String,
        val startDate: String,
        val endDate: String,
        val daysType: String,
        val timeOfTaking: String
    )
}