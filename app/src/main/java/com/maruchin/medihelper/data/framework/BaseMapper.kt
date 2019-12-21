package com.maruchin.medihelper.data.framework

abstract class BaseMapper<T> {

    abstract suspend fun entityToMap(entity: T): Map<String, Any?>

    abstract suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): T
}