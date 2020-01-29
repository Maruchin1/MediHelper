package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity

data class Plan(
    override val entityId: String,
    val profileId: String,
    val medicineId: String,
    val planType: Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) : BaseEntity() {

    enum class Type {
        ONE_DAY, PERIOD, CONTINUOUS
    }
}