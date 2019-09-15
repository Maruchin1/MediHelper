package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import com.example.medihelper.AppDate
import com.example.medihelper.AppTime
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import java.sql.Time


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