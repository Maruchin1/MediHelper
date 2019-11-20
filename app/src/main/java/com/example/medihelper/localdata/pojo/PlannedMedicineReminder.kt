package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.domain.entities.AppTime

data class PlannedMedicineReminder(

    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineId: Int,

    @ColumnInfo(name = "person_name")
    val personName: String,

    @ColumnInfo(name = "person_color_res_id")
    val personColorResId: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "planned_time")
    val plannedTime: AppTime
)