package com.maruchin.medihelper.domain.framework

import androidx.lifecycle.LiveData

interface BaseRepo<T : BaseEntity> {
    suspend fun addNew(entity: T): String?
    suspend fun update(entity: T)
    suspend fun deleteById(entityId: String)
    suspend fun getById(entityId: String): T?
    suspend fun getLiveById(entityId: String): LiveData<T>
    suspend fun getAllList(): List<T>
    suspend fun getAllListLive(): LiveData<List<T>>
}