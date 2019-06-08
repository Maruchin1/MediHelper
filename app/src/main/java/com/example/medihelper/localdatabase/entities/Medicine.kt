package com.example.medihelper.localdatabase.entities

import androidx.room.*

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
    var medicineTypeID: Int,

    @ColumnInfo(name = "package_size")
    var packageSize: Float,

    @ColumnInfo(name = "curr_state")
    var currState: Float,

    @ColumnInfo(name = "photo_file_path")
    var photoFilePath: String,

    @ColumnInfo(name = "expire_date")
    var expireDate: String,

    var comments: String
) {
    fun calcMedicineState() = currState.div(packageSize)
}