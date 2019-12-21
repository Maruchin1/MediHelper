package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.BaseMapper
import com.maruchin.medihelper.domain.entities.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileMapper : BaseMapper<Profile>() {

    private val name = "name"
    private val color = "color"
    private val mainPerson = "mainPerson"

    override suspend fun entityToMap(entity: Profile): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            name to entity.name,
            color to entity.color,
            mainPerson to entity.mainPerson
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): Profile = withContext(Dispatchers.Default) {
        return@withContext Profile(
            entityId = entityId,
            name = map[name] as String,
            color = map[color] as String,
            mainPerson = map[mainPerson] as Boolean
        )
    }
}