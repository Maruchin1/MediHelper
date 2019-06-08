package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.medihelper.localdatabase.entities.ScheduledMedicine

@Dao
interface ScheduledMedicineDAO {
    @Insert
    fun insertSingle(scheduledMedicine: ScheduledMedicine)

    @Insert
    fun insertAll(list: List<ScheduledMedicine>)

    @Query("SELECT * FROM scheduled_medicines WHERE date = :date ORDER BY time ASC")
    fun getByDate(date: String): LiveData<List<ScheduledMedicine>>
}