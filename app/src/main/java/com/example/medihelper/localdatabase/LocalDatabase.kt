package com.example.medihelper.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medihelper.localdatabase.dao.*
import com.example.medihelper.localdatabase.dao.PlannedMedicineDAO
import com.example.medihelper.localdatabase.entities.*

@Database(
    entities = [
        MedicineEntity::class,
        MedicinePlanEntity::class,
        PlannedMedicineEntity::class,
        PersonEntity::class
    ],
    version = 26,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDAO
    abstract fun medicinePlanDao(): MedicinePlanDAO
    abstract fun plannedMedicineDao(): PlannedMedicineDAO
    abstract fun personDao(): PersonDAO

    companion object {
        private const val DATABASE_NAME = "local-database"

        @Volatile
        private var instance: LocalDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): LocalDatabase {
            return Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}