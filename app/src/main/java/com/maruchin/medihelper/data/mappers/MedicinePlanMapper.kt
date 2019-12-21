package com.maruchin.medihelper.data.mappers

import com.maruchin.medihelper.data.framework.BaseMapper
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan

//class MedicinePlanMapper : BaseMapper<MedicinePlan>() {
//
//    private val profileId = "profileId"
//    private val medicineId = "medicineId"
//    private val planType = "planType"
//    private val startDate = "startDate"
//    private val endDate = "endDate"
//    private val intakeDays = "intakeDays"
//    private val timeDoseList = "timeDoseList"
//    //IntakeDays.DaysOfWeek
//    private val monday = "monday"
//    private val tuesday = "tuesday"
//    private val wednesday = "wednesday"
//    private val thursday = "thursday"
//    private val friday = "friday"
//    private val saturday = "saturday"
//    private val sunday = "sunday"
//    //IntakeDays.Interval
//    private val daysCount = "daysCount"
//    //IntakeDays.Sequence
//    private val intakeCount = "intakeCount"
//    private val notIntakeCount = "notIntakeCount"
//
//    override fun entityToMap(entity: MedicinePlan): Map<String, Any?> {
//        return hashMapOf(
//            profileId to entity.profileId,
//            medicineId to entity.medicineId,
//            planType to entity.planType,
//            startDate to entity.startDate.jsonFormatString,
//            endDate to entity.endDate?.jsonFormatString,
//            intakeDays to entity.intakeDays,
//            timeDoseList to entity.timeDoseList
//        )
//    }
//
//    override fun mapToEntity(entityId: String, map: Map<String, Any?>): MedicinePlan {
//        return MedicinePlan(
//            entityId = entityId,
//            profileId = map[profileId] as String,
//            medicineId = map[medicineId] as String,
//            planType = MedicinePlan.Type.valueOf(map[planType] as String),
//            startDate = AppDate(map[startDate] as String),
//            endDate = (map[endDate] as String?)?.let { AppDate(it) },
//            intakeDays = (map[intakeDays] as Map<String, Any?>?)?.let {
//                intakeDaysMapToEntity(it)
//            }
//        )
//    }
//
//    private fun intakeDaysMapToEntity(map: Map<String, Any?>): IntakeDays {
//
//    }
//}