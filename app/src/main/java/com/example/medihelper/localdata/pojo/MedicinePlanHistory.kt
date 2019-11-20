package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.medihelper.data.local.model.PlannedMedicineEntity

data class MedicinePlanHistory(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanId: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @Relation(
        entity = PlannedMedicineEntity::class,
        parentColumn = "medicine_plan_id",
        entityColumn = "medicine_plan_id")
    val medicinePlanHistoryCheckboxList: List<MedicinePlanHistoryCheckbox>
)