package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlanPeriod
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.model.MedicinePlanPeriodValidator
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo

class SaveMedicinePlanPeriodUseCase(
    private val medicinePlanRepo: MedicinePlanRepo
) {
    suspend fun execute(params: Params): MedicinePlanPeriodValidator {
        val validator = MedicinePlanPeriodValidator(
            profileId = params.profileId,
            medicineId = params.medicineId,
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
        val medicinePlan = MedicinePlanPeriod(
            medicinePlanId = params.medicinePlanId ?: "",
            profileId = params.profileId!!,
            medicineId = params.medicineId!!,
            startDate = params.startDate!!,
            endDate = params.endDate!!,
            intakeDays = params.intakeDays!!,
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
        val startDate: AppDate?,
        val endDate: AppDate?,
        val intakeDays: IntakeDays?,
        val timeDoseList: List<TimeDose>?
    )
}