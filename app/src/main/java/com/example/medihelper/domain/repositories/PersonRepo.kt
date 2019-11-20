package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.Person

interface PersonRepo {
    suspend fun insert(person: Person)
    suspend fun update(person: Person)
    suspend fun deleteById(id: Int)
    suspend fun getById(id: Int): Person
    suspend fun getMainId(): Int?
    fun getLiveById(id: Int): LiveData<Person>
    fun getMainLive(): LiveData<Person>
    fun getMainIdLive(): LiveData<Int>
    fun getMainPersonColorIdLive(): LiveData<Int>
    fun getAllListLive(): LiveData<List<Person>>
    fun getListLiveByMedicineId(id: Int): LiveData<List<Person>>
    fun getColorIdList(): List<Int>
}