package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.entity.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.MedicineItem


interface MedicineRepository : ServerSyncRepository<MedicineEntity> {

    suspend fun insert(medicineEntity: MedicineEntity)

    suspend fun update(medicineEntity: MedicineEntity)

    suspend fun delete(medicineID: Int)

    suspend fun getEntity(medicineID: Int): MedicineEntity

    suspend fun getEntityList(): List<MedicineEntity>

    suspend fun getDetails(medicineID: Int): MedicineDetails

    fun getItemListLive(): LiveData<List<MedicineItem>>

    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>

    fun getItemLive(medicineID: Int): LiveData<MedicineItem>

    fun getDetailsLive(medicineID: Int): LiveData<MedicineDetails>
}
