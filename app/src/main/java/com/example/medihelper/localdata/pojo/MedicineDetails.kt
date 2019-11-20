package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.domain.entities.AppExpireDate
import com.example.medihelper.domain.entities.Medicine


data class MedicineDetails(
    @ColumnInfo(name = "medicine_id")
    val medicineId: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    val expireDate: AppExpireDate?,

    @ColumnInfo(name = "package_size")
    val packageSize: Float?,

    @ColumnInfo(name = "curr_state")
    val currState: Float?,

    @ColumnInfo(name = "additional_info")
    val additionalInfo: String?,

    @ColumnInfo(name = "image_name")
    var imageName: String?
)