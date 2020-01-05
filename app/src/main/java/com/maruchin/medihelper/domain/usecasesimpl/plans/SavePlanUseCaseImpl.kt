package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlanErrors
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plans.SavePlanUseCase
import com.maruchin.medihelper.domain.utils.MedicinePlanValidator
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SavePlanUseCaseImpl(
    private val planRepo: PlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val plannedMedicineScheduler: PlannedMedicineScheduler,
    private val deviceCalendar: DeviceCalendar,
    private val validator: MedicinePlanValidator
) : SavePlanUseCase {

    private lateinit var useCaseParams: SavePlanUseCase.Params

    override suspend fun execute(params: SavePlanUseCase.Params): PlanErrors =
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

    private fun getMedicinePlan(): Plan {
        return Plan(
            entityId = useCaseParams.medicinePlanId ?: "",
            profileId = useCaseParams.profileId!!,
            medicineId = useCaseParams.medicineId!!,
            planType = useCaseParams.planType!!,
            startDate = useCaseParams.startDate!!,
            endDate = if (useCaseParams.planType == Plan.Type.PERIOD) {
                useCaseParams.endDate!!
            } else null,
            intakeDays = if (useCaseParams.planType != Plan.Type.ONE_DAY) {
                useCaseParams.intakeDays!!
            } else null,
            timeDoseList = useCaseParams.timeDoseList!!
        )
    }

    private suspend fun addNewMedicinePlan(plan: Plan) {
        val addedMedicinePlanId = planRepo.addNew(plan)
        if (addedMedicinePlanId != null) {
            val addedMedicinePlan = plan.copy(entityId = addedMedicinePlanId)
            addNewPlannedMedicines(addedMedicinePlan)
        }
    }

    private suspend fun updateMedicinePlan(plan: Plan) {
        planRepo.update(plan)
        updatePlannedMedicines(plan)
    }

    private suspend fun addNewPlannedMedicines(plan: Plan) {
        val plannedMedicines = plannedMedicineScheduler.getPlannedMedicines(plan)
        plannedMedicineRepo.addNewList(plannedMedicines)
    }

    private suspend fun updatePlannedMedicines(plan: Plan) {
        deleteExistingPlannedMedicinesFromNow(plan)
        scheduleNewPlannedMedicinesFromNow(plan)
    }

    private suspend fun deleteExistingPlannedMedicinesFromNow(plan: Plan) {
        val existingPlannedMedicinesIdsFromNow = getExistingPlannedMedicinesIdsFromNow(plan.entityId)
        plannedMedicineRepo.deleteListById(existingPlannedMedicinesIdsFromNow)
    }

    private suspend fun scheduleNewPlannedMedicinesFromNow(plan: Plan) {
        val scheduledPlannedMedicines = getScheduledPlannedMedicinesFromNow(plan)
        plannedMedicineRepo.addNewList(scheduledPlannedMedicines)
    }

    private suspend fun getExistingPlannedMedicinesIdsFromNow(medicinePlanId: String): List<String> {
        val currDate = deviceCalendar.getCurrDate()
        val existingPlannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        return existingPlannedMedicines.filter {
            it.plannedDate >= currDate
        }.map { it.entityId }
    }

    private suspend fun getScheduledPlannedMedicinesFromNow(plan: Plan): List<PlannedMedicine> {
        val currDate = deviceCalendar.getCurrDate()
        val medicinePlanFromNow = plan.copy(startDate = currDate)
        return plannedMedicineScheduler.getPlannedMedicines(medicinePlanFromNow)
    }
}