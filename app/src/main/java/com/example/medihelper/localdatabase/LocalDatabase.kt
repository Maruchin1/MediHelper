package com.example.medihelper.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.medihelper.localdatabase.dao.MedicineDAO
import com.example.medihelper.localdatabase.dao.MedicineTypeDAO
import com.example.medihelper.localdatabase.dao.ScheduledMedicineDAO
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import com.example.medihelper.localdatabase.entities.ScheduledMedicine

@Database(
    entities = [
        Medicine::class,
        MedicineType::class,
        ScheduledMedicine::class
    ],
    version = 16,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun medicineDao(): MedicineDAO
    abstract fun medicineTypeDao(): MedicineTypeDAO
    abstract fun scheduledMedicineDao(): ScheduledMedicineDAO

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