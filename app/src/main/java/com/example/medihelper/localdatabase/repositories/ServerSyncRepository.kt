package com.example.medihelper.localdatabase.repositories

interface ServerSyncRepository<T> {

    suspend fun insertSynchronized(entityList: List<T>)

    suspend fun updateSynchronized(entityList: List<T>)

    suspend fun resetSynchronizationData()

    suspend fun deleteByRemoteIDNotIn(remoteIDList: List<Long>)

    suspend fun deleteAll()

    suspend fun clearDeletedRemoteIDList()

    suspend fun getEntityListToSync(): List<T>

    suspend fun getDeletedRemoteIDList(): List<Long>

    suspend fun getRemoteID(localID: Int): Long?

    suspend fun getLocalIDByRemoteID(remoteID: Long): Int?
}