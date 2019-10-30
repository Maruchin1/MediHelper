package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity

data class MedicinePlanHistory(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @Relation(
        entity = PlannedMedicineEntity::class,
        parentColumn = "medicine_plan_id",
        entityColumn = "medicine_plan_id")
    val medicinePlanHistoryCheckboxList: List<MedicinePlanHistoryCheckbox>
)