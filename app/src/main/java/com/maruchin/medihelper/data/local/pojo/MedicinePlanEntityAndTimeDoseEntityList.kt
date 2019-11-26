package com.maruchin.medihelper.data.local.pojo

import androidx.room.Embedded
import androidx.room.Relation
import com.maruchin.medihelper.data.local.model.MedicinePlanEntity
import com.maruchin.medihelper.data.local.model.TimeDoseEntity
import com.maruchin.medihelper.domain.entities.MedicinePlan

data class MedicinePlanEntityAndTimeDoseEntityList(
    @Embedded
    val medicinePlanEntity: MedicinePlanEntity,

    @Relation(
        entity = TimeDoseEntity::class,
        parentColumn = "medicine_plan_id",
        entityColumn = "medicine_plan_id"
    )
    val timeDoseEntityList: List<TimeDoseEntity>
) {
    fun toMedicinePlan() = MedicinePlan(
        medicinePlanId = medicinePlanEntity.medicinePlanId,
        medicineId = medicinePlanEntity.medicineId,
        personId = medicinePlanEntity.personId,
        durationType = medicinePlanEntity.durationType,
        startDate = medicinePlanEntity.startDate,
        endDate = medicinePlanEntity.endDate,
        daysType = medicinePlanEntity.daysType,
        daysOfWeek = medicinePlanEntity.daysOfWeek,
        intervalOfDays = medicinePlanEntity.intervalOfDays,
        timeDoseList = timeDoseEntityList.map { it.toTimeDose() }
    )
}