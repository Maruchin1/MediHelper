package com.example.medihelper.localdatabase.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.localdatabase.AppDate
import com.example.medihelper.localdatabase.AppTime
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity


data class MedicinePlanHistoryCheckbox(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineID: Int,

    @ColumnInfo(name = "planned_date")
    val plannedDate: AppDate,

    @ColumnInfo(name = "planned_time")
    val plannedTime: AppTime,

    @ColumnInfo(name = "status_of_taking")
    val statusOfTaking: PlannedMedicineEntity.StatusOfTaking
)