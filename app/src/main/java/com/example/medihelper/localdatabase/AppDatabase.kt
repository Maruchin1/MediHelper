package com.example.medihelper.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medihelper.localdatabase.entities.*
import com.example.medihelper.localdatabase.repositoriesimpl.MedicineDao
import com.example.medihelper.localdatabase.repositoriesimpl.MedicinePlanDao
import com.example.medihelper.localdatabase.repositoriesimpl.PersonDao
import com.example.medihelper.localdatabase.repositoriesimpl.PlannedMedicineDao

@Database(
    entities = [
        MedicineEntity::class,
        MedicinePlanEntity::class,
        PlannedMedicineEntity::class,
        PersonEntity::class
    ],
    version = 31,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun medicinePlanDao(): MedicinePlanDao
    abstract fun plannedMedicineDao(): PlannedMedicineDao
    abstract fun personDao(): PersonDao

    companion object {
        const val DATABASE_NAME = "local-database"
    }
}