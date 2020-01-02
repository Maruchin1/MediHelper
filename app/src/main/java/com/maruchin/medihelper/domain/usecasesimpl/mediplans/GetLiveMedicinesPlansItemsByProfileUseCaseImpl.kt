package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanItem
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.mediplans.GetLiveMedicinesPlansItemsByProfileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLiveMedicinesPlansItemsByProfileUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val medicineRepo: MedicineRepo
) : GetLiveMedicinesPlansItemsByProfileUseCase {
    private lateinit var selectedProfileId: String

    override suspend fun execute(profileId: String): LiveData<List<MedicinePlanItem>> =
        withContext(Dispatchers.Default) {
            selectedProfileId = profileId
            val allMedicinesPlansLive = medicinePlanRepo.getAllListLive()
            return@withContext Transformations.switchMap(allMedicinesPlansLive) { plans ->
                getLiveItemsForPlans(plans)
            }
        }

    private fun getLiveItemsForPlans(plans: List<MedicinePlan>): LiveData<List<MedicinePlanItem>> {
        return liveData {
            val items = getItemsForPlans(plans)
            emit(items)
        }
    }

    private suspend fun getItemsForPlans(plans: List<MedicinePlan>): List<MedicinePlanItem> =
        withContext(Dispatchers.Default) {
            val plansByProfile = filterPlansByProfileId(plans)
            return@withContext mapPlansToItems(plansByProfile)
        }

    private fun filterPlansByProfileId(plans: List<MedicinePlan>): List<MedicinePlan> {
        return plans.filter { plan ->
            plan.profileId == selectedProfileId
        }
    }

    private suspend fun mapPlansToItems(plans: List<MedicinePlan>): List<MedicinePlanItem> {
        return plans.map { plan ->
            val medicine = getMedicine(plan.medicineId)
            MedicinePlanItem(plan, medicine)
        }
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}