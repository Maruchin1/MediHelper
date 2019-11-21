package com.example.medihelper.data.local.model

import androidx.room.*
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.PlannedMedicine
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
    val plannedMedicineId: Int,

    @ColumnInfo(name = "planned_medicine_remote_id")
    var plannedMedicineRemoteId: Long?,

    @ColumnInfo(name = "medicine_plan_id")
    var medicinePlanId: Int,

    @ColumnInfo(name = "planned_date")
    var plannedDate: AppDate,

    @ColumnInfo(name = "planned_time")
    var plannedTime: AppTime,

    @ColumnInfo(name = "planned_dose_size")
    var plannedDoseSize: Float,

    @ColumnInfo(name = "status_of_taking")
    var statusOfTaking: StatusOfTaking,

    @ColumnInfo(name = "planned_medicine_synchronized")
    var plannedMedicineSynchronized: Boolean
) {
    constructor(plannedMedicine: PlannedMedicine) : this(
        plannedMedicineId = 0,
        plannedMedicineRemoteId = null,
        medicinePlanId = plannedMedicine.medicinePlanId,
        plannedDate = plannedMedicine.plannedDate,
        plannedTime = plannedMedicine.plannedTime,
        plannedDoseSize = plannedMedicine.plannedDoseSize,
        statusOfTaking = plannedMedicine.statusOfTaking,
        plannedMedicineSynchronized = false
    )

    fun update(plannedMedicine: PlannedMedicine) {
        medicinePlanId = plannedMedicine.medicinePlanId
        plannedDate = plannedMedicine.plannedDate
        plannedTime = plannedMedicine.plannedTime
        plannedDoseSize = plannedMedicine.plannedDoseSize
        statusOfTaking = plannedMedicine.statusOfTaking
        plannedMedicineSynchronized = false
    }

    fun toPlannedMedicine() = PlannedMedicine(
        plannedMedicineId = plannedMedicineId,
        medicinePlanId = medicinePlanId,
        plannedDate = plannedDate,
        plannedTime = plannedTime,
        plannedDoseSize = plannedDoseSize,
        statusOfTaking = statusOfTaking
    )
}