package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.model.PlannedMedicineItem

data class PlannedMedicineItemData(
    val plannedMedicineId: String,
    val medicineName: String,
    val plannedDose: String,
    val plannedTime: String,
    val statusData: PlannedMedicineStatusData
) {

    companion object {

        fun fromDomainModel(domainModel: PlannedMedicineItem): PlannedMedicineItemData {
            return PlannedMedicineItemData(
                plannedMedicineId = domainModel.plannedMedicineId,
                medicineName = domainModel.medicineName,
                plannedDose = "${domainModel.plannedDoseSize} ${domainModel.medicineUnit}",
                plannedTime = domainModel.plannedTime.formatString,
                statusData = PlannedMedicineStatusData.fromDomainModel(domainModel.status)
            )
        }
    }
}