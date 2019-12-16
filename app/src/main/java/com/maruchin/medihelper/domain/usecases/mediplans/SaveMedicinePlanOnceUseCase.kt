package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.MedicinePlanOnceValidator
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo

class SaveMedicinePlanOnceUseCase(
    private val medicinePlanRepo: MedicinePlanRepo
) {
    suspend fun execute(params: Params): MedicinePlanOnceValidator {
        val validator = MedicinePlanOnceValidator(
            profileId = params.profileId,
            medicineId = params.medicineId,
            date = params.date,
            timeDoseList = params.timeDoseList
        )
        if (validator.noErrors) {
            saveMedicinePlanToRepo(params)
        }
        return validator
    }

    private suspend fun saveMedicinePlanToRepo(params: Params) {
        val medicinePlan = MedicinePlanOnce(
            medicinePlanId = params.medicinePlanId ?: "",
            profileId = params.profileId!!,
            medicineId = params.medicineId!!,
            date = params.date!!,
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
        val date: AppDate?,
        val timeDoseList: List<TimeDose>?
    )
}