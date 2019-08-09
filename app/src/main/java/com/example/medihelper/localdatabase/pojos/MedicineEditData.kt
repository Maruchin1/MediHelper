package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.example.medihelper.localdatabase.entities.MedicineTypeEntity
import java.util.*

data class MedicineEditData(
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @Embedded
    val medicineType: MedicineTypeEntity?,

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