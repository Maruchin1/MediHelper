package com.example.medihelper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medihelper.data.local.dao.*
import com.example.medihelper.data.local.model.*

@Database(
    entities = [
        MedicineEntity::class,
        MedicinePlanEntity::class,
        PlannedMedicineEntity::class,
        PersonEntity::class,
        TimeDoseEntity::class
    ],
    version = 46,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun medicinePlanDao(): MedicinePlanDao
    abstract fun plannedMedicineDao(): PlannedMedicineDao
    abstract fun personDao(): PersonDao
    abstract fun timeDoseDao(): TimeDoseDao

    companion object {
        const val MAIN_DATABASE_NAME = "main-database-name"
        const val CONNECTED_DATABASE_NAME = "connected-person-database-name"
    }
}