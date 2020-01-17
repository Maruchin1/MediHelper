package com.maruchin.medihelper.domain.framework

import androidx.lifecycle.LiveData

interface BaseTypeRepo {
    suspend fun init(types: List<String>)
    suspend fun addNewDistinct(type: String)
    suspend fun delete(type: String)
    suspend fun getAll(): List<String>
    suspend fun getLiveAll(): LiveData<List<String>>
}