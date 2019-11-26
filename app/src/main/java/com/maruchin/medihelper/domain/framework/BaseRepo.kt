package com.maruchin.medihelper.domain.framework

import androidx.lifecycle.LiveData

interface BaseRepo<T> {
    suspend fun addNew(entity: T)
    suspend fun update(entity: T)
    suspend fun deleteById(id: String)
    suspend fun getById(id: String): T?
    suspend fun getLiveById(id: String): LiveData<T?>
    suspend fun getAllList(): List<T>
    suspend fun getAllListLive(): LiveData<List<T>>
}