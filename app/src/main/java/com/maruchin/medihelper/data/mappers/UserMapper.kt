package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.BaseMapper
import com.maruchin.medihelper.domain.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserMapper : BaseMapper<User>() {

    private val userName = "userName"
    private val email = "email"

    override suspend fun entityToMap(entity: User): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            userName to entity.userName,
            email to entity.email
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): User = withContext(Dispatchers.Default) {
        return@withContext User(
            entityId = entityId,
            userName = map[userName] as String,
            email = map[email] as String
        )
    }
}