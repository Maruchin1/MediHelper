package com.example.medihelper.localdata.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.domain.entities.AppExpireDate

data class MedicineEditData(

    @ColumnInfo(name = "medicine_id")
    val medicineId: Int = 0,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    val expireDate: AppExpireDate,

    @ColumnInfo(name = "package_size")
    val packageSize: Float? = null,

    @ColumnInfo(name = "curr_state")
    val currState: Float? = null,

    @ColumnInfo(name = "additional_info")
    val additionalInfo: String? = null,

    @ColumnInfo(name = "image_name")
    val imageName: String? = null
)