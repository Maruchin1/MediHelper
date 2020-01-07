package com.maruchin.medihelper.data.mappers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TypeContainerMapper {

    private val typesField = "types"

    suspend fun containerToMap(container: TypeContainer): Map<String, Any> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            typesField to container.types
        )
    }

    suspend fun mapToContainer(map: Map<String, Any>): TypeContainer = withContext(Dispatchers.Default) {
        return@withContext TypeContainer(
            types = map[typesField] as List<String>
        )
    }
}