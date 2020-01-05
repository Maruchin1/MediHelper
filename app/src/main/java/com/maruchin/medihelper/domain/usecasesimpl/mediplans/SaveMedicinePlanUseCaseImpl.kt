package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.MedicinePlanErrors
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.mediplans.SaveMedicinePlanUseCase
import com.maruchin.medihelper.domain.utils.MedicinePlanValidator
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMedicinePlanUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val plannedMedicineScheduler: PlannedMedicineScheduler,
    private val deviceCalendar: DeviceCalendar,
    private val validator: MedicinePlanValidator
) : SaveMedicinePlanUseCase {

    private lateinit var useCaseParams: SaveMedicinePlanUseCase.Params

    override suspend fun execute(params: SaveMedicinePlanUseCase.Params): MedicinePlanErrors =
        withContext(Dispatchers.Default) {
            useCaseParams = params
            val validatorParams = getValidatorParams()
            val errors = validator.validate(validatorParams)

            if (errors.noErrors) {
                saveMedicinePlanToRepo()
            }
            return@withContext errors
        }

    private fun getValidatorParams(): MedicinePlanValidator.Params {
        return MedicinePlanValidator.Params(
            profileId = useCaseParams.profileId,
            medicineId = useCaseParams.medicineId,
            planType = useCaseParams.planType,
            startDate = useCaseParams.startDate,
            endDate = useCaseParams.endDate,
            intakeDays = useCaseParams.intakeDays,
            timeDoseList = useCaseParams.timeDoseList
        )
    }

    private suspend fun saveMedicinePlanToRepo() {
        val medicinePlan = getMedicinePlan()
        if (useCaseParams.medicinePlanId == null) {
            addNewMedicinePlan(medicinePlan)
        } else {
            updateMedicinePlan(medicinePlan)
        }
    }

    private fun getMedicinePlan(): MedicinePlan {
        return MedicinePlan(
            entityId = useCaseParams.medicinePlanId ?: "",
            profileId = useCaseParams.profileId!!,
            medicineId = useCaseParams.medicineId!!,
            planType = useCaseParams.planType!!,
            startDate = useCaseParams.startDate!!,
            endDate = if (useCaseParams.planType == MedicinePlan.Type.PERIOD) {
                useCaseParams.endDate!!
            } else null,
            intakeDays = if (useCaseParams.planType != MedicinePlan.Type.ONCE) {
                useCaseParams.intakeDays!!
            } else null,
            timeDoseList = useCaseParams.timeDoseList!!
        )
    }

    private suspend fun addNewMedicinePlan(medicinePlan: MedicinePlan) {
        val addedMedicinePlanId = medicinePlanRepo.addNew(medicinePlan)
        if (addedMedicinePlanId != null) {
            val addedMedicinePlan = medicinePlan.copy(entityId = addedMedicinePlanId)
            addNewPlannedMedicines(addedMedicinePlan)
        }
    }

    private suspend fun updateMedicinePlan(medicinePlan: MedicinePlan) {
        medicinePlanRepo.update(medicinePlan)
        updatePlannedMedicines(medicinePlan)
    }

    private suspend fun addNewPlannedMedicines(medicinePlan: MedicinePlan) {
        val plannedMedicines = plannedMedicineScheduler.getPlannedMedicines(medicinePlan)
        plannedMedicineRepo.addNewList(plannedMedicines)
    }

    private suspend fun updatePlannedMedicines(medicinePlan: MedicinePlan) {
        deleteExistingPlannedMedicinesFromNow(medicinePlan)
        scheduleNewPlannedMedicinesFromNow(medicinePlan)
    }

    private suspend fun deleteExistingPlannedMedicinesFromNow(medicinePlan: MedicinePlan) {
        val existingPlannedMedicinesIdsFromNow = getExistingPlannedMedicinesIdsFromNow(medicinePlan.entityId)
        plannedMedicineRepo.deleteListById(existingPlannedMedicinesIdsFromNow)
    }

    private suspend fun scheduleNewPlannedMedicinesFromNow(medicinePlan: MedicinePlan) {
        val scheduledPlannedMedicines = getScheduledPlannedMedicinesFromNow(medicinePlan)
        plannedMedicineRepo.addNewList(scheduledPlannedMedicines)
    }

    private suspend fun getExistingPlannedMedicinesIdsFromNow(medicinePlanId: String): List<String> {
        val currDate = deviceCalendar.getCurrDate()
        val existingPlannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        return existingPlannedMedicines.filter {
            it.plannedDate >= currDate
        }.map { it.entityId }
    }

    private suspend fun getScheduledPlannedMedicinesFromNow(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val currDate = deviceCalendar.getCurrDate()
        val medicinePlanFromNow = medicinePlan.copy(startDate = currDate)
        return plannedMedicineScheduler.getPlannedMedicines(medicinePlanFromNow)
    }
}