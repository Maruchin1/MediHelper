package com.maruchin.medihelper.presentation.feature.calendar

data class PlannedMedicineItemCheckBoxData(
    val plannedMedicineId: String,
    val colorId: Int,
    val iconId: Int
) {
    constructor(itemData: PlannedMedicineItemData): this(
        plannedMedicineId = itemData.plannedMedicineId,
        colorId = itemData.statusData.colorId,
        iconId = itemData.statusData.iconId
    )
}