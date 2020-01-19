package com.maruchin.medihelper.presentation.feature.calendar

data class PlannedMedicineItemCheckBoxData(
    val colorId: Int,
    val iconId: Int
) {
    constructor(itemData: PlannedMedicineItemData): this(
        colorId = itemData.statusData.colorId,
        iconId = itemData.statusData.iconId
    )
}