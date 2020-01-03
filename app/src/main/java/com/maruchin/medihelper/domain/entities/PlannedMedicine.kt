package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity

data class PlannedMedicine(
    override val entityId: String,
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val plannedDate: AppDate,
    var plannedTime: AppTime,
    val plannedDoseSize: Float,
    var status: Status
) : BaseEntity() {

    enum class Status {
        TAKEN, NOT_TAKEN
    }
}