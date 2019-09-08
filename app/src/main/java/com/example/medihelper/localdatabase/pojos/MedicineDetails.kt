package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.medihelper.localdatabase.entities.PersonEntity
import java.util.*

class MedicineDetails(
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int,

    @ColumnInfo(name = "medicine_name")
    val medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    val medicineUnit: String,

    @ColumnInfo(name = "package_size")
    val packageSize: Float?,

    @ColumnInfo(name = "curr_state")
    val currState: Float?,

    @ColumnInfo(name = "photo_file_path")
    val photoFilePath: String?,

    @ColumnInfo(name = "expire_date")
    val expireDate: Date?,

    @ColumnInfo(name = "commentsLive")
    val comments: String?
)