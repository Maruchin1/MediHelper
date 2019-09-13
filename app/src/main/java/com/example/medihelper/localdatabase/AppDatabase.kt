package com.example.medihelper.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
    version = 26,
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

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }

}