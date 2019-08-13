package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonListItem

@Dao
interface PersonDAO {

    @Insert
    fun insert(person: PersonEntity)

    @Update
    fun update(person: PersonEntity)

    @Delete
    fun delete(person: PersonEntity)

    @Query("SELECT * FROM persons")
    fun getListItemListLive(): LiveData<List<PersonListItem>>
}