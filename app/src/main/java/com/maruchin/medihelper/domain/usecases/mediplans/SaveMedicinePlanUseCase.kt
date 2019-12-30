package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.MedicinePlanErrors
import com.maruchin.medihelper.domain.utils.MedicinePlanValidator
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMedicinePlanUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val plannedMedicineScheduler: PlannedMedicineScheduler,
    private val deviceCalendar: DeviceCalendar,
    private val deviceReminder: DeviceReminder,
    private val validator: MedicinePlanValidator
) {
    suspend fun execute(params: Params): MedicinePlanErrors = withContext(Dispatchers.Default) {
        val validatorParams = MedicinePlanValidator.Params(
            profileId = params.profileId,
            medicineId = params.medicineId,
            planType = params.planType,
            startDate = params.startDate,
            endDate = params.endDate,
            intakeDays = params.intakeDays,
            timeDoseList = params.timeDoseList
        )
        val errors = validator.validate(validatorParams)

        if (errors.noErrors) {
            saveMedicinePlanToRepo(params)
        }
        return@withContext errors
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
                addNewPlannedMedicines(addedMedicinePlan)
            }
        } else {
            medicinePlanRepo.update(medicinePlan)
            updatePlannedMedicines(medicinePlan)
        }
    }

    private suspend fun addNewPlannedMedicines(medicinePlan: MedicinePlan) {
        val plannedMedicines = plannedMedicineScheduler.getPlannedMedicines(medicinePlan)

        val addedPlannedMedicines = plannedMedicineRepo.addNewList(plannedMedicines)
        deviceReminder.addReminders(addedPlannedMedicines)
    }

    private suspend fun updatePlannedMedicines(medicinePlan: MedicinePlan) {
        val currDate = deviceCalendar.getCurrDate()
        val existingPlannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlan.entityId)
        val existingPlannedMedicinesFromNow = existingPlannedMedicines.filter {
            it.plannedDate >= currDate
        }

        plannedMedicineRepo.deleteListById(existingPlannedMedicinesFromNow.map { it.entityId })
        deviceReminder.cancelReminders(existingPlannedMedicinesFromNow)

        val medicinePlanFromNow = medicinePlan.copy(startDate = currDate)
        val scheduledPlannedMedicines = plannedMedicineScheduler.getPlannedMedicines(medicinePlanFromNow)

        val addedPlannedMedicines = plannedMedicineRepo.addNewList(scheduledPlannedMedicines)
        deviceReminder.addReminders(addedPlannedMedicines)
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