package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineDetails(
    val plannedMedicineId: String,
    val medicinePlanId: String,
    val medicineName: String,
    val medicineUnit: String,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    val taken: Boolean,
    val statusColorId: Int,
    val statusIconId: Int
) {
    constructor(plannedMedicine: PlannedMedicine, medicine: Medicine) : this(
        plannedMedicineId = plannedMedicine.entityId,
        medicinePlanId = plannedMedicine.medicinePlanId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        plannedDate = plannedMedicine.plannedDate,
        plannedDoseSize = plannedMedicine.plannedDoseSize,
        plannedTime = plannedMedicine.plannedTime,
        taken = plannedMedicine.taken,
        statusColorId = if (plannedMedicine.taken) R.color.colorStateGood else R.color.colorDarkerGray,
        statusIconId = if (plannedMedicine.taken) R.drawable.round_check_circle_24 else R.drawable.round_radio_button_unchecked_24
    )
}