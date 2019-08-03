package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.util.*

@Dao
interface ScheduledMedicineDAO {
    @Insert
    fun insertSingle(scheduledMedicine: ScheduledMedicine)

    @Insert
    fun insertAll(list: List<ScheduledMedicine>)

    @Query("SELECT * FROM scheduled_medicines")
    fun getAllLive(): LiveData<List<ScheduledMedicine>>

    @Query("SELECT * FROM scheduled_medicines")
    fun getAll(): List<ScheduledMedicine>

    @Delete
    fun delete(scheduledMedicine: ScheduledMedicine)

    @Update
    fun update(scheduledMedicine: ScheduledMedicine)
}