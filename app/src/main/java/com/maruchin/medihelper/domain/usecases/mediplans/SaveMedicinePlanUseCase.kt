package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.MedicinePlanValidator
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMedicinePlanUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val plannedMedicineScheduler: PlannedMedicineScheduler,
    private val deviceCalendar: DeviceCalendar
) {
    suspend fun execute(params: Params): MedicinePlanValidator = withContext(Dispatchers.Default) {
        val validator = MedicinePlanValidator(
            profileId = params.profileId,
            medicineId = params.medicineId,
            planType = params.planType,
            startDate = params.startDate,
            endDate = params.endDate,
            intakeDays = params.intakeDays,
            timeDoseList = params.timeDoseList
        )
        if (validator.noErrors) {
            saveMedicinePlanToRepo(params)
        }
        return@withContext validator
    }

    private suspend fun saveMedicinePlanToRepo(params: Params) {
        val medicinePlan = MedicinePlan(
            entityId = params.medicinePlanId ?: "",
            profileId = params.profileId!!,
            medicineId = params.medicineId!!,
            planType = params.planType!!,
            startDate = params.startDate!!,
            endDate = if (params.planType == MedicinePlan.Type.PERIOD) {
                params.endDate!!
            } else null,
            intakeDays = if (params.planType != MedicinePlan.Type.ONCE) {
                params.intakeDays!!
            } else null,
            timeDoseList = params.timeDoseList!!
        )
        if (params.medicinePlanId == null) {
            val addedMedicinePlanId = medicinePlanRepo.addNew(medicinePlan)
            addedMedicinePlanId?.let {
                val addedMedicinePlan = medicinePlan.copy(entityId = it)
                addNewCalendarEntries(addedMedicinePlan)
            }
        } else {
            medicinePlanRepo.update(medicinePlan)
            updateExistingCalendarEntries(medicinePlan)
        }
    }

    private suspend fun addNewCalendarEntries(medicinePlan: MedicinePlan) {
        val plannedMedicines = plannedMedicineScheduler.getPlannedMedicines(medicinePlan)
        plannedMedicineRepo.addNewList(plannedMedicines)
    }

    private suspend fun updateExistingCalendarEntries(medicinePlan: MedicinePlan) {
        val currTimeInMillis = deviceCalendar.getCurrTimeInMillis()
        val currDate = AppDate(currTimeInMillis)
        val currTime = AppTime(currTimeInMillis)

        val existingPlannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlan.entityId)
        val existingPlannedMedicinesIdFromNow = existingPlannedMedicines.filter {
            it.plannedDate >= currDate && it.plannedTime >= currTime
        }.map {
            it.entityId
        }
        plannedMedicineRepo.deleteListById(existingPlannedMedicinesIdFromNow)

        val medicinePlanFromNow = medicinePlan.copy(startDate = currDate)
        val scheduledPlannedMedicines = plannedMedicineScheduler.getPlannedMedicines(medicinePlanFromNow)
        plannedMedicineRepo.addNewList(scheduledPlannedMedicines)
    }

    data class Params(
        val medicinePlanId: String?,
        val profileId: String?,
        val medicineId: String?,
        val planType: MedicinePlan.Type?,
        val startDate: AppDate?,
        val endDate: AppDate?,
        val intakeDays: IntakeDays?,
        val timeDoseList: List<TimeDose>?
    )
}