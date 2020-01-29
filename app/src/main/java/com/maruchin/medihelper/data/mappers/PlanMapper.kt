package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.EntityMapper
import com.maruchin.medihelper.domain.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanMapper : EntityMapper<Plan>() {

    private enum class IntakeDaysType {
        EVERYDAY, DAYS_OF_WEEK, INTERVAL, SEQUENCE
    }

    private val profileId = "profileId"
    private val medicineId = "medicineId"
    private val planType = "planType"
    private val startDate = "startDate"
    private val endDate = "endDate"
    private val intakeDays = "intakeDays"
    private val timeDoseList = "timeDoseList"
    private val takenMedicines = "takenMedicines"

    private val intakeDaysType = "intakeDaysType"
    //IntakeDays.DaysOfWeek
    private val monday = "monday"
    private val tuesday = "tuesday"
    private val wednesday = "wednesday"
    private val thursday = "thursday"
    private val friday = "friday"
    private val saturday = "saturday"
    private val sunday = "sunday"
    //IntakeDays.Interval
    private val daysCount = "daysCount"
    //IntakeDays.Sequence
    private val intakeCount = "intakeCount"
    private val notIntakeCount = "notIntakeCount"
    //TimeDose
    private val time = "time"
    private val doseSize = "doseSize"
    //TakenMedicine
    private val plannedDate = "plannedDate"
    private val plannedTime = "plannedTime"

    override suspend fun entityToMap(entity: Plan): Map<String, Any?> = withContext(Dispatchers.Default) {
        return@withContext hashMapOf(
            profileId to entity.profileId,
            medicineId to entity.medicineId,
            planType to entity.planType,
            startDate to entity.startDate.jsonFormatString,
            endDate to entity.endDate?.jsonFormatString,
            intakeDays to entity.intakeDays?.let { intakeDaysEntityToMap(it) },
            timeDoseList to timeDoseEntityListToMapList(entity.timeDoseList),
            takenMedicines to takenMedicineEntityListToMapList(entity.takenMedicines)
        )
    }

    override suspend fun mapToEntity(entityId: String, map: Map<String, Any?>): Plan =
        withContext(Dispatchers.Default) {
            return@withContext Plan(
                entityId = entityId,
                profileId = map[profileId] as String,
                medicineId = map[medicineId] as String,
                planType = Plan.Type.valueOf(map[planType] as String),
                startDate = AppDate(map[startDate] as String),
                endDate = (map[endDate] as String?)?.let { AppDate(it) },
                intakeDays = (map[intakeDays] as Map<String, Any?>?)?.let {
                    intakeDaysMapToEntity(it)
                },
                timeDoseList = timeDoseMapListToEntityList(map[timeDoseList] as List<Map<String, Any?>>),
                takenMedicines = takenMedicineMapListToEntityList(map[takenMedicines] as List<Map<String, Any?>>)
            )
        }

    private fun intakeDaysMapToEntity(map: Map<String, Any?>): IntakeDays {
        val type = IntakeDaysType.valueOf(map[intakeDaysType] as String)
        return when (type) {
            IntakeDaysType.EVERYDAY -> IntakeDays.Everyday
            IntakeDaysType.INTERVAL -> IntakeDays.Interval(
                daysCount = (map[daysCount] as Long).toInt()
            )
            IntakeDaysType.SEQUENCE -> IntakeDays.Sequence(
                intakeCount = (map[intakeCount] as Long).toInt(),
                notIntakeCount = (map[notIntakeCount] as Long).toInt()
            )
            IntakeDaysType.DAYS_OF_WEEK -> IntakeDays.DaysOfWeek(
                monday = map[monday] as Boolean,
                tuesday = map[tuesday] as Boolean,
                wednesday = map[wednesday] as Boolean,
                thursday = map[thursday] as Boolean,
                friday = map[friday] as Boolean,
                saturday = map[saturday] as Boolean,
                sunday = map[sunday] as Boolean
            )
        }
    }

    private fun intakeDaysEntityToMap(intakeDays: IntakeDays): Map<String, Any?> {
        return when (intakeDays) {
            is IntakeDays.Everyday -> hashMapOf(
                intakeDaysType to IntakeDaysType.EVERYDAY
            )
            is IntakeDays.DaysOfWeek -> hashMapOf(
                intakeDaysType to IntakeDaysType.DAYS_OF_WEEK,
                monday to intakeDays.monday,
                tuesday to intakeDays.tuesday,
                wednesday to intakeDays.wednesday,
                thursday to intakeDays.thursday,
                friday to intakeDays.friday,
                saturday to intakeDays.saturday,
                sunday to intakeDays.sunday
            )
            is IntakeDays.Interval -> hashMapOf(
                intakeDaysType to IntakeDaysType.INTERVAL,
                daysCount to intakeDays.daysCount
            )
            is IntakeDays.Sequence -> hashMapOf(
                intakeDaysType to IntakeDaysType.SEQUENCE,
                intakeCount to intakeDays.intakeCount,
                notIntakeCount to intakeDays.notIntakeCount
            )
        }
    }

    private fun timeDoseMapListToEntityList(mapList: List<Map<String, Any?>>): List<TimeDose> {
        return mapList.map { map ->
            TimeDose(
                time = AppTime(map[time] as String),
                doseSize = (map[doseSize] as Double).toFloat()
            )
        }
    }

    private fun timeDoseEntityListToMapList(entityList: List<TimeDose>): List<Map<String, Any?>> {
        return entityList.map { timeDose ->
            hashMapOf(
                time to timeDose.time.jsonFormatString,
                doseSize to timeDose.doseSize
            )
        }
    }

    private fun takenMedicineMapListToEntityList(mapList: List<Map<String, Any?>>): List<TakenMedicine> {
        return mapList.map { map ->
            TakenMedicine(
                plannedDate = AppDate(map[plannedDate] as String),
                plannedTime = AppTime(map[plannedTime] as String)
            )
        }
    }

    private fun takenMedicineEntityListToMapList(entityList: List<TakenMedicine>): List<Map<String, Any?>> {
        return entityList.map { takenMedicine ->
            hashMapOf(
                plannedDate to takenMedicine.plannedDate.jsonFormatString,
                plannedTime to takenMedicine.plannedTime.jsonFormatString
            )
        }
    }
}