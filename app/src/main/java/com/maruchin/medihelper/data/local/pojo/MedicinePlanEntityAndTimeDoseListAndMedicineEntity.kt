package com.maruchin.medihelper.data.local.pojo

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import com.maruchin.medihelper.data.local.ImagesFiles
import com.maruchin.medihelper.data.local.model.TimeDoseEntity
import com.maruchin.medihelper.domain.entities.*

data class MedicinePlanEntityAndTimeDoseListAndMedicineEntity(

    @ColumnInfo(name = "medicine_plan_id")
    val medicinePlanId: Int,

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

    @ColumnInfo(name = "person_id")
    var personId: Int,

    @ColumnInfo(name = "medicine_id")
    val medicineId: Int,

    @ColumnInfo(name = "medicine_name")
    var medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    var medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    var expireDate: AppExpireDate,

    @ColumnInfo(name = "package_size")
    var packageSize: Float?,

    @ColumnInfo(name = "curr_state")
    var currState: Float?,

    @ColumnInfo(name = "additional_info")
    var additionalInfo: String?,

    @ColumnInfo(name = "image_name")
    var imageName: String?,

    @Relation(
        entity = TimeDoseEntity::class,
        parentColumn = "medicine_plan_id",
        entityColumn = "medicine_plan_id"
    )
    val timeDoseEntityList: List<TimeDoseEntity>
) {
    fun toMedicinePlanWithMedicine(imagesFiles: ImagesFiles) = MedicinePlanWithMedicine(
        medicinePlanId = medicinePlanId,
        medicine = Medicine(
            medicineId = medicineId,
            name = medicineName,
            unit = medicineUnit,
            expireDate = expireDate,
            packageSize = packageSize,
            currState = currState,
            additionalInfo = additionalInfo,
            image = imageName?.let { imagesFiles.getImageFile(it) }
        ),
        personId = personId,
        durationType = durationType,
        startDate = startDate,
        endDate = endDate,
        daysType = daysType,
        daysOfWeek = daysOfWeek,
        intervalOfDays = intervalOfDays,
        timeDoseList = timeDoseEntityList.map { it.toTimeDose() }
    )
}