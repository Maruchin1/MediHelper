package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository

class MedicineRepositoryImpl(private val medicineDao: MedicineDao) :
    MedicineRepository {

    override suspend fun insert(medicineEntity: MedicineEntity) = medicineDao.insert(medicineEntity)

    override suspend fun insert(medicineEntityList: List<MedicineEntity>) = medicineDao.insert(medicineEntityList)

    override suspend fun update(medicineEntity: MedicineEntity) = medicineDao.update(medicineEntity)

    override suspend fun delete(medicineID: Int) = medicineDao.delete(medicineID)

    override suspend fun deleteAll() = medicineDao.deleteAll()

    override suspend fun getEntity(medicineID: Int) = medicineDao.getEntity(medicineID)

    override suspend fun getEntityList() = medicineDao.getEntityList()

    override suspend fun getDetails(medicineID: Int) = medicineDao.getDetails(medicineID)

    override fun getItemListLive(): LiveData<List<MedicineItem>> = medicineDao.getItemListLive()

    override fun getFilteredItemListLive(searchQuery: String) = medicineDao.getFilteredItemListLive(searchQuery)

    override fun getItemLive(medicineID: Int) = medicineDao.getItemLive(medicineID)

    override fun getDetailsLive(medicineID: Int) = medicineDao.getDetailsLive(medicineID)
}

@Dao
interface MedicineDao {

    @Insert
    suspend fun insert(medicineEntity: MedicineEntity)

    @Insert
    suspend fun insert(medicineEntityList: List<MedicineEntity>)

    @Update
    suspend fun update(medicineEntity: MedicineEntity)

    @Query("DELETE FROM medicines WHERE medicine_id = :medicineID")
    suspend fun delete(medicineID: Int)

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    suspend fun getEntity(medicineID: Int): MedicineEntity

    @Query("SELECT * FROM medicines")
    suspend fun getEntityList(): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    suspend fun getDetails(medicineID: Int): MedicineDetails

    @Query("SELECT * FROM medicines")
    fun getItemListLive(): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE LOWER(medicine_name) LIKE '%' || LOWER(:searchQuery) || '%'")
    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getItemLive(medicineID: Int): LiveData<MedicineItem>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getDetailsLive(medicineID: Int): LiveData<MedicineDetails>
}