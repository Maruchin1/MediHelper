package com.maruchin.medihelper.data.local.model

import androidx.room.*
import com.maruchin.medihelper.domain.entities.*

@Entity(
    tableName = "medicines_plans",
    foreignKeys = [
        ForeignKey(
            entity = MedicineEntity::class,
            parentColumns = arrayOf("medicine_id"),
            childColumns = arrayOf("medicine_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = arrayOf("person_id"),
            childColumns = arrayOf("person_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medicine_id"])]
)
data class MedicinePlanEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanId: Int,

    @ColumnInfo(name = "medicine_plan_remote_id")
    var medicinePlanRemoteId: Long?,

    @ColumnInfo(name = "medicine_id")
    var medicineId: Int,

    @ColumnInfo(name = "person_id")
    var personId: Int,

    @ColumnInfo(name = "duration_type")
    var durationType: DurationType,

    @ColumnInfo(name = "start_date")
    var startDate: AppDate,

    @ColumnInfo(name = "end_date")
    var endDate: AppDate?,

    @ColumnInfo(name = "days_type")
    var daysType: DaysType?,

    @Embedded
    var daysOfWeek: DaysOfWeek?,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int?,

    @ColumnInfo(name = "medicine_plan_synchronized")
    var medicinePlanSynchronized: Boolean
) {
    constructor(medicinePlan: MedicinePlan) : this(
        medicinePlanId = 0,
        medicinePlanRemoteId = null,
        medicineId = medicinePlan.medicineId,
        personId = medicinePlan.personId,
        durationType = medicinePlan.durationType,
        startDate = medicinePlan.startDate,
        endDate = medicinePlan.endDate,
        daysType = medicinePlan.daysType,
        daysOfWeek = medicinePlan.daysOfWeek,
        intervalOfDays = medicinePlan.intervalOfDays,
        medicinePlanSynchronized = false
    )

    fun update(medicinePlan: MedicinePlan) {
        medicineId = medicinePlan.medicineId
        personId = medicinePlan.personId
        durationType = medicinePlan.durationType
        startDate = medicinePlan.startDate
        endDate = medicinePlan.endDate
        daysType = medicinePlan.daysType
        daysOfWeek = medicinePlan.daysOfWeek
        intervalOfDays = medicinePlan.intervalOfDays
        medicinePlanSynchronized = false
    }
}