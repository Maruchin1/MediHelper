package com.example.medihelper.data.local.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.data.local.ImagesFiles
import com.example.medihelper.domain.entities.*

data class PlannedMedicineEntityAndMedicineEntityAndPersonEntity(
    @ColumnInfo(name = "planned_medicine_id")
    val plannedMedicineId: Int,

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

    @ColumnInfo(name = "person_id")
    val personId: Int,

    @ColumnInfo(name = "person_remote_id")
    var personRemoteId: Long?,

    @ColumnInfo(name = "person_name")
    var personName: String,

    @ColumnInfo(name = "person_color_res_id")
    var personColorResId: Int,

    @ColumnInfo(name = "main_person")
    var mainPerson: Boolean,

    @ColumnInfo(name = "connection_key")
    var connectionKey: String?
) {
    fun toPlannedMedicineWithMedicineAndPerson(imagesFiles: ImagesFiles) = PlannedMedicineWithMedicineAndPerson(
        plannedMedicineId = plannedMedicineId,
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
        person = Person(
            personId = personId,
            name = personName,
            colorId = personColorResId,
            mainPerson = mainPerson,
            connectionKey = connectionKey
        ),
        plannedDate = plannedDate,
        plannedTime = plannedTime,
        plannedDoseSize = plannedDoseSize,
        statusOfTaking = statusOfTaking
    )
}