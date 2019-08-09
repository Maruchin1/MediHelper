package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import java.util.*

class MedicineDetails(
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "type_name")
    val typeName: String?,

    @ColumnInfo(name = "package_size")
    val packageSize: Float?,

    @ColumnInfo(name = "curr_state")
    val currState: Float?,

    @ColumnInfo(name = "photo_file_path")
    var photoFilePath: String?,

    @ColumnInfo(name = "expire_date")
    var expireDate: Date?,

    var comments: String?
)