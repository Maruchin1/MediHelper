package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.BaseMapper
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MedicineMapper : BaseMapper<Medicine>() {

    private val name = "name"
    private val unit = "unit"
    private val expireDate = "expireDate"
    private val type = "type"
    private val state = "state"
    private val pictureName = "pictureName"
    //MedicineStateData
    private val packageSize = "packageSize"
    private val currState = "currState"

    override suspend fun entityToMap(entity: Medicine): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            name to entity.name,
            unit to entity.unit,
            expireDate to entity.expireDate?.jsonFormatString,
            type to entity.type,
            state to stateDataToMap(entity.state),
            pictureName to entity.pictureName
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): Medicine = withContext(Dispatchers.Default) {
        return@withContext Medicine(
            entityId = entityId,
            name = map[name] as String,
            unit = map[unit] as String,
            expireDate = map[expireDate]?.let { AppExpireDate(it as String) },
            type = map[type] as String?,
            state = mapToStateData(map[state] as Map<String, Any?>),
            pictureName = map[pictureName] as String?
        )
    }

    private fun stateDataToMap(state: MedicineState): Map<String, Any?> {
        return hashMapOf(
            packageSize to state.packageSize,
            currState to state.currState
        )
    }

    private fun mapToStateData(map: Map<String, Any?>): MedicineState {
        return MedicineState(
            packageSize = (map[packageSize] as Double).toFloat(),
            currState = (map[currState] as Double).toFloat()
        )
    }
}