package com.maruchin.medihelper.domain.framework

interface BaseTypeRepo {
    suspend fun addNewDistinct(type: String)
    suspend fun delete(type: String)
    suspend fun getAll(): List<String>
}