package com.example.medihelper.data.local.model

import androidx.room.*
import com.example.medihelper.R
import com.example.medihelper.domain.entities.AppExpireDate
import com.example.medihelper.domain.entities.Medicine

@Entity(tableName = "medicines")
data class MedicineEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_id")
    val medicineId: Int = 0,

    @ColumnInfo(name = "medicine_remote_id")
    var medicineRemoteId: Long? = null,

    @ColumnInfo(name = "medicine_name")
    var medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    var medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    var expireDate: AppExpireDate,

    @ColumnInfo(name = "package_size")
    var packageSize: Float? = null,

    @ColumnInfo(name = "curr_state")
    var currState: Float? = null,

    @ColumnInfo(name = "additional_info")
    var additionalInfo: String? = null,

    @ColumnInfo(name = "image_name")
    var imageName: String? = null,

    @ColumnInfo(name = "synchronized_with_server")
    var synchronizedWithServer: Boolean = false
) {
    constructor(medicine: Medicine, medicineRemoteId: Long?) : this(
        medicineId = medicine.medicineId,
        medicineRemoteId = medicineRemoteId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        expireDate = medicine.expireDate,
        packageSize = medicine.packageSize,
        currState = medicine.currState,
        additionalInfo = medicine.additionalInfo,
        imageName = medicine.image?.name
    )

    fun toMedicine() = Medicine(
        medicineId = medicineId,
        name = medicineName,
        unit = medicineUnit,
        expireDate = expireDate,
        packageSize = packageSize,
        currState = currState,
        additionalInfo = additionalInfo,
        image =
    )
}