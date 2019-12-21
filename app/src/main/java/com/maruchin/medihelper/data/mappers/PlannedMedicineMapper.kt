package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.BaseMapper
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlannedMedicineMapper : BaseMapper<PlannedMedicine>() {

    private val medicinePlanId = "medicinePlanId"
    private val profileId = "profileId"
    private val medicineId = "medicineId"
    private val plannedDate = "plannedDate"
    private val plannedTime = "plannedTime"
    private val plannedDoseSize = "plannedDoseSize"
    private val taken = "taken"

    override suspend fun entityToMap(entity: PlannedMedicine): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            medicinePlanId to entity.medicinePlanId,
            profileId to entity.profileId,
            medicineId to entity.medicineId,
            plannedDate to entity.plannedDate.jsonFormatString,
            plannedTime to entity.plannedTime.jsonFormatString,
            plannedDoseSize to entity.plannedDoseSize,
            taken to entity.taken
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): PlannedMedicine = withContext(Dispatchers.Default) {
        return@withContext PlannedMedicine(
            entityId = entityId,
            medicinePlanId = map[medicinePlanId] as String,
            profileId = map[profileId] as String,
            medicineId = map[medicineId] as String,
            plannedDate = AppDate(map[plannedDate] as String),
            plannedTime = AppTime(map[plannedTime] as String),
            plannedDoseSize = (map[plannedDoseSize] as Double).toFloat(),
            taken = map[taken] as Boolean
        )
    }
}