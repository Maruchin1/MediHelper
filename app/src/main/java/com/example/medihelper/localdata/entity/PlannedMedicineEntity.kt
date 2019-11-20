package com.example.medihelper.localdata.entity

import androidx.room.*
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.StatusOfTaking

@Entity(
    tableName = "planned_medicines",
    foreignKeys = [ForeignKey(
        entity = MedicinePlanEntity::class,
        parentColumns = arrayOf("medicine_plan_id"),
        childColumns = arrayOf("medicine_plan_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["medicine_plan_id"])]
)
data class PlannedMedicineEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineId: Int = 0,

    @ColumnInfo(name = "planned_medicine_remote_id")
    var plannedMedicineRemoteId: Long? = null,

    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanId: Int,

    @ColumnInfo(name = "planned_date")
    var plannedDate: AppDate,

    @ColumnInfo(name = "planned_time")
    var plannedTime: AppTime,

    @ColumnInfo(name = "planned_dose_size")
    var plannedDoseSize: Float,

    @ColumnInfo(name = "status_of_taking")
    var statusOfTaking: StatusOfTaking = StatusOfTaking.WAITING,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
)