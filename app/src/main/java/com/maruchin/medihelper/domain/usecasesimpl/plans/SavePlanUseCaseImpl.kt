package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanErrors
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.plans.SavePlanUseCase
import com.maruchin.medihelper.domain.utils.MedicinePlanValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SavePlanUseCaseImpl(
    private val planRepo: PlanRepo,
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
        val plan = getMedicinePlan()
        if (useCaseParams.medicinePlanId == null) {
            planRepo.addNew(plan)
        } else {
            planRepo.update(plan)
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
}