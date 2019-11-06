package com.example.medihelper.localdatabase.pojo

import androidx.room.ColumnInfo
import com.example.medihelper.AppDate

data class MedicineEditData(

    @ColumnInfo(name = "medicine_id")
    val medicineID: Int = 0,

    @ColumnInfo(name = "medicine_remote_id")
    var medicineRemoteID: Long? = null,

    @ColumnInfo(name = "medicine_name")
    var medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    var medicineUnit: String,

    @ColumnInfo(name = "expire_date")
    var expireDate: AppDate? = null,

    @ColumnInfo(name = "package_size")
    var packageSize: Float? = null,

    @ColumnInfo(name = "curr_state")
    var currState: Float? = null,

    @ColumnInfo(name = "additional_info")
    var additionalInfo: String? = null,

    @ColumnInfo(name = "image_name")
    var imageName: String? = null
)