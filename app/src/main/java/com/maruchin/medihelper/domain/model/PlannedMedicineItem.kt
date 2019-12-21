package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineItem(
    val plannedMedicineId: String,
    val medicineName: String,
    val medicineUnit: String,
    val plannedDoseSize: Float,
    val plannedTime: AppTime,
    val statusColorId: Int,
    val statusIconId: Int
) {
    constructor(plannedMedicine: PlannedMedicine, medicine: Medicine) : this(
        plannedMedicineId = plannedMedicine.entityId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        plannedDoseSize = plannedMedicine.plannedDoseSize,
        plannedTime = plannedMedicine.plannedTime,
        statusColorId = if (plannedMedicine.taken) R.color.colorStateGood else R.color.colorDarkerGray,
        statusIconId = if (plannedMedicine.taken) R.drawable.round_check_circle_24 else R.drawable.round_radio_button_unchecked_24
    )
}