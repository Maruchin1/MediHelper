package com.example.medihelper.service

import com.example.medihelper.domain.entities.*

class FormValidatorService {

    fun isMedicineValid(
        medicineName: String?,
        expireDate: AppExpireDate?,
        packageSize: Float?,
        currState: Float?
    ) = MedicineError(
        emptyName = medicineName.isNullOrEmpty(),
        emptyExpireDate = expireDate == null,
        currStateBiggerThanPackageSize = packageSize != null &&
                currState != null &&
                currState > packageSize
    )

    fun isPersonValid(personName: String?) = PersonError(
        emptyName = personName.isNullOrEmpty()
    )

    fun isMedicinePlanValid(
        medicineId: Int?,
        startDate: AppDate?,
        endDate: AppDate?,
        durationType: DurationType?,
        daysType: DaysType?,
        daysOfWeek: DaysOfWeek?
    ) = MedicinePlanError(
        emptyMedicine = medicineId == null,
        emptyStartDate = startDate == null,
        emptyEndDate = durationType == DurationType.PERIOD && endDate == null,
        incorrectDatesOrder = durationType == DurationType.PERIOD &&
                startDate != null &&
                endDate != null &&
                startDate >= endDate,
        emptyDaysOfWeek = daysType == DaysType.DAYS_OF_WEEK &&
                daysOfWeek != null &&
                daysOfWeek.run {
                    arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday).none { it }
                }
    )

    data class MedicineError(
        val emptyName: Boolean,
        val emptyExpireDate: Boolean,
        val currStateBiggerThanPackageSize: Boolean
    )

    data class PersonError(
        val emptyName: Boolean
    )

    data class MedicinePlanError(
        val emptyMedicine: Boolean,
        val emptyStartDate: Boolean,
        val emptyEndDate: Boolean,
        val incorrectDatesOrder: Boolean,
        val emptyDaysOfWeek: Boolean
    )
}