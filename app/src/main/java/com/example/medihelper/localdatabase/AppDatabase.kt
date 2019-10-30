package com.example.medihelper.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medihelper.localdatabase.entity.*
import com.example.medihelper.localdatabase.repositoriesimpl.*

@Database(
    entities = [
        MedicineEntity::class,
        MedicinePlanEntity::class,
        PlannedMedicineEntity::class,
        PersonEntity::class,
        DeletedEntity::class
    ],
    version = 41,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDao
    abstract fun medicinePlanDao(): MedicinePlanDao
    abstract fun plannedMedicineDao(): PlannedMedicineDao
    abstract fun personDao(): PersonDao
    abstract fun deletedEntityDao(): DeletedEntityDao

    companion object {
        const val MAIN_DATABASE_NAME = "main-database-name"
        const val CONNECTED_PERSON_DATABASE_NAME = "connected-person-database-name"
    }
}