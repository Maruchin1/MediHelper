package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.pojos.PersonSimpleItem

@Dao
interface PersonDAO {

    @Insert
    fun insert(person: PersonEntity): Long

    @Update
    fun update(person: PersonEntity)

    @Delete
    fun delete(person: PersonEntity)

    @Query("SELECT * FROM persons")
    fun getListItemListLive(): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons WHERE person_id == :personID")
    fun getListItemLive(personID: Int): LiveData<PersonItem>

    @Query("SELECT * FROM persons")
    fun getSimpleItemListLive(): LiveData<List<PersonSimpleItem>>

    @Query("SELECT * FROM persons WHERE person_id == :personID")
    fun getSimpleItemLive(personID: Int): LiveData<PersonSimpleItem>
}