package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.Medicine

interface MedicineRepo {
    suspend fun insert(medicine: Medicine)
    suspend fun update(medicine: Medicine)
    suspend fun deleteById(id: Int)
    suspend fun getById(id: Int): Medicine
    fun getLiveById(id: Int): LiveData<Medicine>
    fun getAllListLive(): LiveData<List<Medicine>>
    fun getListLiveFilteredByName(nameQuery: String): LiveData<List<Medicine>>
    fun getUnitList(): List<String>
    fun getUnitListLive(): LiveData<List<String>>
    fun saveUnitList(list: List<String>)
}