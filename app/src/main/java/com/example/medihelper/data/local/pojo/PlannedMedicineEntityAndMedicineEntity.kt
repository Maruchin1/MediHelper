package com.example.medihelper.data.local.pojo

import androidx.room.Embedded
import com.example.medihelper.data.local.model.MedicineEntity
import com.example.medihelper.data.local.model.PlannedMedicineEntity
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine

data class PlannedMedicineEntityAndMedicineEntity(
    @Embedded
    val plannedMedicineEntity: PlannedMedicineEntity,
    @Embedded
    val medicineEntity: MedicineEntity
) {
    fun toPlannedMedicineWithMedicine() = PlannedMedicineWithMedicine(
        plannedMedicineId = plannedMedicineEntity.plannedMedicineId,
        medicine = medicineEntity.toMedicine(),
        plannedDate = plannedMedicineEntity.plannedDate,
        plannedTime = plannedMedicineEntity.plannedTime,
        plannedDoseSize = plannedMedicineEntity.plannedDoseSize,
        statusOfTaking = plannedMedicineEntity.statusOfTaking
    )
}