package com.example.medihelper.localdatabase.pojos

import androidx.room.ColumnInfo

data class MedicineKitItem(
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
    val photoFilePath: String?
) {
    fun calcMedicineState(): Float? {
        return packageSize?.let { packageSize ->
            currState?.div(packageSize)
        }
    }
}