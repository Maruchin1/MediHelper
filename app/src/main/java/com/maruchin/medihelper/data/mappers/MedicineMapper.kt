package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.BaseMapper
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicineMapper : BaseMapper<Medicine>() {

    private val name = "name"
    private val unit = "unit"
    private val expireDate = "expireDate"
    private val packageSize = "packageSize"
    private val currState = "currState"
    private val pictureName = "pictureName"

    override suspend fun entityToMap(entity: Medicine): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            name to entity.name,
            unit to entity.unit,
            expireDate to entity.expireDate.jsonFormatString,
            packageSize to entity.packageSize,
            currState to entity.currState,
            pictureName to entity.pictureName
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): Medicine = withContext(Dispatchers.Default) {
        return@withContext Medicine(
            entityId = entityId,
            name = map[name] as String,
            unit = map[unit] as String,
            expireDate = AppExpireDate(map[expireDate] as String),
            packageSize = (map[packageSize] as Double).toFloat(),
            currState = (map[currState] as Double).toFloat(),
            pictureName = map[pictureName] as String?
        )
    }
}