package com.maruchin.medihelper.domain.usecasesimpl.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanItem
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plans.GetLivePlansItemsByProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLivePlansItemsByProfileUseCaseImpl(
    private val planRepo: PlanRepo,
    private val medicineRepo: MedicineRepo
) : GetLivePlansItemsByProfileUseCase {
    private lateinit var selectedProfileId: String

    override suspend fun execute(profileId: String): LiveData<List<PlanItem>> =
        withContext(Dispatchers.Default) {
            selectedProfileId = profileId
            val allMedicinesPlansLive = planRepo.getAllListLive()
            return@withContext Transformations.switchMap(allMedicinesPlansLive) { plans ->
                getLiveItemsForPlans(plans)
            }
        }

    private fun getLiveItemsForPlans(plans: List<Plan>): LiveData<List<PlanItem>> {
        return liveData {
            val items = getItemsForPlans(plans)
            emit(items)
        }
    }

    private suspend fun getItemsForPlans(plans: List<Plan>): List<PlanItem> =
        withContext(Dispatchers.Default) {
            val plansByProfile = filterPlansByProfileId(plans)
            return@withContext mapPlansToItems(plansByProfile)
        }

    private fun filterPlansByProfileId(plans: List<Plan>): List<Plan> {
        return plans.filter { plan ->
            plan.profileId == selectedProfileId
        }
    }

    private suspend fun mapPlansToItems(plans: List<Plan>): List<PlanItem> {
        return plans.map { plan ->
            val medicine = getMedicine(plan.medicineId)
            PlanItem(plan, medicine)
        }
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}