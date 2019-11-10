package com.example.medihelper.localdatabase.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.custom.AppDate


data class MedicineDetails(
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    val expireDate: AppDate?,

    @ColumnInfo(name = "package_size")
    val packageSize: Float?,

    @ColumnInfo(name = "curr_state")
    val currState: Float?,

    @ColumnInfo(name = "additional_info")
    val additionalInfo: String?,

    @ColumnInfo(name = "image_name")
    var imageName: String?
)