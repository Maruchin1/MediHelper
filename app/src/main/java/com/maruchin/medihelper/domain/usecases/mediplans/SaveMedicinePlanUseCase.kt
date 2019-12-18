package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.model.MedicinePlanValidator
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo

class SaveMedicinePlanUseCase(
    private val medicinePlanRepo: MedicinePlanRepo
) {
    suspend fun execute(params: Params): MedicinePlanValidator {
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
        return validator
    }

    private suspend fun saveMedicinePlanToRepo(params: Params) {
        val medicinePlan = MedicinePlan(
            medicinePlanId = params.medicinePlanId ?: "",
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
            medicinePlanRepo.addNew(medicinePlan)
        } else {
            medicinePlanRepo.update(medicinePlan)
        }
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