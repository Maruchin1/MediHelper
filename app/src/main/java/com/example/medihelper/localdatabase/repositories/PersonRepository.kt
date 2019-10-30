package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.pojos.PersonOptionsData

interface PersonRepository : ServerSyncRepository<PersonEntity> {

    suspend fun insert(personEntity: PersonEntity): Int

    suspend fun update(personEntity: PersonEntity)

    suspend fun delete(personID: Int)

    suspend fun deleteAllWithMain()

    suspend fun getEntity(personID: Int): PersonEntity

    suspend fun getEntityList(): List<PersonEntity>

    suspend fun getItem(personID: Int): PersonItem

    suspend fun getMainPersonID(): Int?

    suspend fun getMainPersonEntity(): PersonEntity

    fun getItemLive(personID: Int): LiveData<PersonItem>

    fun getOptionsDataLive(personID: Int): LiveData<PersonOptionsData>

    fun getItemListLive(): LiveData<List<PersonItem>>

    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>

    fun getMainPersonIDLive(): LiveData<Int>

    fun getMainPersonItemLive(): LiveData<PersonItem>

    fun getMainPersonColorLive(): LiveData<Int>
}