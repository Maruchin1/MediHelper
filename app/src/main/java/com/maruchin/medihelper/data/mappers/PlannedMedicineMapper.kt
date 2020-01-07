package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.EntityMapper
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class PlannedMedicineMapper : EntityMapper<PlannedMedicine>() {

    val medicinePlanId = "medicinePlanId"
    val profileId = "profileId"
    val medicineId = "medicineId"
    val plannedDate = "plannedDate"
    val plannedTime = "plannedTime"
    val plannedDateTimeMillis = "plannedDateTimeMillis"
    val plannedDoseSize = "plannedDoseSize"
    val status = "status"

    override suspend fun entityToMap(entity: PlannedMedicine): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            medicinePlanId to entity.medicinePlanId,
            profileId to entity.profileId,
            medicineId to entity.medicineId,
            plannedDate to entity.plannedDate.jsonFormatString,
            plannedTime to entity.plannedTime.jsonFormatString,
            plannedDateTimeMillis to calculateDateTimeMillis(entity),
            plannedDoseSize to entity.plannedDoseSize,
            status to entity.status.toString()
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): PlannedMedicine =
        withContext(Dispatchers.Default) {
            return@withContext PlannedMedicine(
                entityId = entityId,
                medicinePlanId = map[medicinePlanId] as String,
                profileId = map[profileId] as String,
                medicineId = map[medicineId] as String,
                plannedDate = AppDate(map[plannedDate] as String),
                plannedTime = AppTime(map[plannedTime] as String),
                plannedDoseSize = (map[plannedDoseSize] as Double).toFloat(),
                status = PlannedMedicine.Status.valueOf(map[status] as String)
            )
        }

    private fun calculateDateTimeMillis(entity: PlannedMedicine): Long {
        val calendar = Calendar.getInstance().apply {
            set(
                entity.plannedDate.year,
                entity.plannedDate.month - 1,
                entity.plannedDate.day,
                entity.plannedTime.hour,
                entity.plannedTime.minute,
                0
            )
        }
        return calendar.timeInMillis
    }
}