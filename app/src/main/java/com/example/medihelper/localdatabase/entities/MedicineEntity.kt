package com.example.medihelper.localdatabase.entities

import androidx.room.*
import java.sql.Date

@Entity(tableName = "medicines")
data class MedicineEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int = 0,

    @ColumnInfo(name = "medicine_name")
    var medicineName: String,

    @ColumnInfo(name = "medicine_unit")
    var medicineUnit: String,

    @ColumnInfo(name = "package_size")
    var packageSize: Float? = null,

    @ColumnInfo(name = "curr_state")
    var currState: Float? = null,

    @ColumnInfo(name = "photo_file_path")
    var photoFilePath: String? = null,

    @ColumnInfo(name = "expire_date")
    var expireDate: Date? = null,

    var comments: String? = null
) {
    fun reduceCurrState(doseSize: Float) {
        currState?.let { currState ->
            this.currState = currState - doseSize
            if (this.currState!! < 0f) {
                this.currState = 0f
            }
        }
    }

    fun increaseCurrState(doseSize: Float) {
        currState?.let { currState ->
            this.currState = currState + doseSize
            if (packageSize != null && this.currState!! > packageSize!!) {
                this.currState = this.packageSize
            }
        }
    }
}