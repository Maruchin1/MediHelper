package com.example.medihelper.data.local.model

import androidx.room.*
import com.example.medihelper.domain.entities.*

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
    val medicinePlanId: Int = 0,

    @ColumnInfo(name = "medicine_plan_remote_id")
    var medicinePlanRemoteId: Long? = null,

    @ColumnInfo(name = "medicine_id")
    var medicineId: Int,

    @ColumnInfo(name = "person_id")
    var personId: Int,

    @ColumnInfo(name = "duration_type")
    var durationType: DurationType,

    @ColumnInfo(name = "start_date")
    var startDate: AppDate,

    @ColumnInfo(name = "end_date")
    var endDate: AppDate? = null,

    @ColumnInfo(name = "days_type")
    var daysType: DaysType? = null,

    @Embedded
    var daysOfWeek: DaysOfWeek? = null,

    @ColumnInfo(name = "interval_of_days")
    var intervalOfDays: Int? = null,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
) {
    constructor(medicinePlan: MedicinePlan, remoteId: Long?) : this(
        medicinePlanId = medicinePlan.medicinePlanId,
        medicinePlanRemoteId = remoteId,
        medicineId = medicinePlan.medicineId,
        personId = medicinePlan.personId,
        durationType = medicinePlan.durationType,
        startDate = medicinePlan.startDate,
        endDate = medicinePlan.endDate,
        daysType = medicinePlan.daysType,
        daysOfWeek = medicinePlan.daysOfWeek,
        intervalOfDays = medicinePlan.intervalOfDays,
        synchronizedWithServer = false
    )
}