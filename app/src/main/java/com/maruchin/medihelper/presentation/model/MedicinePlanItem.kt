package com.maruchin.medihelper.presentation.model

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.*

data class MedicinePlanItem(
    val medicinePlanId: Int,
    val colorPrimaryId: Int,
    val medicineName: String,
    val durationType: String,
    val daysType: String?,
    val isAppModeConnected: LiveData<Boolean>
) {
//    constructor(
//        medicinePlanWithMedicine: MedicinePlanWithMedicine,
//        colorPrimaryId: Int,
//        isAppModeConnected: LiveData<Boolean>
//    ) : this(
//        medicinePlanId = medicinePlanWithMedicine.medicinePlanId,
//        colorPrimaryId = colorPrimaryId,
//        medicineName = medicinePlanWithMedicine.medicine.name,
//        durationType = when (medicinePlanWithMedicine.durationType) {
//            DurationType.ONCE -> "Jednorazowo ${medicinePlanWithMedicine.startDate.formatString}"
//            DurationType.PERIOD -> "Od ${medicinePlanWithMedicine.startDate.formatString} " +
//                    "do ${medicinePlanWithMedicine.endDate?.formatString}"
//            DurationType.CONTINUOUS -> "Pzyjmowanie ciągłe od ${medicinePlanWithMedicine.startDate.formatString}"
//        },
//        daysType = when (medicinePlanWithMedicine.daysType) {
//            DaysType.EVERYDAY -> "Codziennie"
//            DaysType.DAYS_OF_WEEK -> medicinePlanWithMedicine.daysOfWeek?.selectedDaysString ?: "--"
//            DaysType.INTERVAL_OF_DAYS -> "Co ${medicinePlanWithMedicine.intervalOfDays ?: "--"} dni"
//            else -> null
//        },
//        isAppModeConnected = isAppModeConnected
//    )
}