package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import java.util.*

class MedicinePlanItem(
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "start_date")
    var startDate: Date,

    @ColumnInfo(name = "end_date")
    var endDate: Date? = null,

    @ColumnInfo(name = "schedule_type")
    var durationType: MedicinePlanEntity.DurationType,

    @Embedded
    var daysOfWeek: MedicinePlanEntity.DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @ColumnInfo(name = "schedule_days")
    var daysType: MedicinePlanEntity.DaysType,

    @ColumnInfo(name = "dose_hour_list")
    var timeOfTakingList: List<MedicinePlanEntity.TimeOfTaking>
)