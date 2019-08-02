package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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

    @Delete
    fun delete(scheduledMedicine: ScheduledMedicine)
}