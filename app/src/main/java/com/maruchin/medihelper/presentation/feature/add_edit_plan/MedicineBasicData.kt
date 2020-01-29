package com.maruchin.medihelper.presentation.feature.add_edit_plan

import com.maruchin.medihelper.domain.model.MedicineSimpleItem
import com.maruchin.medihelper.domain.model.PlanDetails

data class MedicineBasicData(
    val medicineId: String,
    val name: String,
    val type: String,
    val unit: String
) {
    companion object {

        fun fromDomainModel(model: MedicineSimpleItem) = MedicineBasicData(
            medicineId = model.medicineId,
            name = model.name,
            unit = model.unit,
            type = model.type
        )

        fun fromDomainModel(model: PlanDetails) = MedicineBasicData(
            medicineId = model.medicineId,
            name = model.medicineName,
            type = model.medicineType,
            unit = model.medicineUnit
        )
    }
}