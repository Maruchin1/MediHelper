package com.example.medihelper.localdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
tableName = "medicine_types",
indices = [Index(value = ["type_name"], unique = true)]
)
data class MedicineType (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "medicine_type_id")
    val medicineTypeID: Int? = null,

    @ColumnInfo(name = "type_name")
    var typeName: String
)