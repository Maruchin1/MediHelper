package com.maruchin.medihelper.domain.usecasesimpl.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanCalendarData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.plans.GetLivePlansCalendarDataForProfileUseCase

class GetLivePlansCalendarDataForProfileUseCaseImpl(
    private val planRepo: PlanRepo,
    private val medicineRepo: MedicineRepo
) : GetLivePlansCalendarDataForProfileUseCase {

    override suspend fun execute(profileId: String): LiveData<List<PlanCalendarData>> {
        val plansLive = planRepo.getLiveByProfile(profileId)
        return mapLivePlansToCalendarData(plansLive)
    }

    private suspend fun mapLivePlansToCalendarData(
        plansLive: LiveData<List<Plan>>
    ) = Transformations.switchMap(plansLive) { plans ->
        liveData {
            val value = plans.map { singlePlan ->
                val medicine = medicineRepo.getById(singlePlan.medicineId)!!
                PlanCalendarData(
                    plan = singlePlan,
                    medicine = medicine
                )
            }
            emit(value)
        }
    }
}