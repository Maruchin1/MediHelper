package com.example.medihelper.localdatabase.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = "medicines",
    foreignKeys = [ForeignKey(
        entity = MedicineType::class,
        parentColumns = arrayOf("medicine_type_id"),
        childColumns = arrayOf("medicine_type_id")
    )],
    indices = [Index(value = ["medicine_type_id"])]
)
data class Medicine(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_id")
    val medicineID: Int? = null,

    var name: String,

    @ColumnInfo(name = "medicine_type_id")
    var medicineTypeID: Int? = null,

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
    fun calcMedicineState(): Float? {
        return if (packageSize != null && currState != null) {
            currState?.div(packageSize!!)
        } else {
            null
        }
    }
}