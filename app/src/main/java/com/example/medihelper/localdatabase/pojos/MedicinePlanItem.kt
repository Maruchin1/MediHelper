package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import java.sql.Date


data class MedicinePlanItem(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "start_date")
    val startDate: Date,

    @ColumnInfo(name = "end_date")
    val endDate: Date? = null,

    @ColumnInfo(name = "schedule_type")
    val durationType: MedicinePlanEntity.DurationType,

    @Embedded
    val daysOfWeek: MedicinePlanEntity.DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    val intervalOfDays: Int? = null,

    @ColumnInfo(name = "schedule_days")
    val daysType: MedicinePlanEntity.DaysType
)