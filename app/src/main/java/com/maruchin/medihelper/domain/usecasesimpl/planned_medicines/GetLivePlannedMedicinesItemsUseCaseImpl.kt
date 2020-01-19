package com.maruchin.medihelper.domain.usecasesimpl.planned_medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsUseCase

class GetLivePlannedMedicinesItemsUseCaseImpl(
    private val planRepo: PlanRepo,
    private val medicineRepo: MedicineRepo
) : GetLivePlannedMedicinesItemsUseCase {

    private lateinit var profileId: String
    private lateinit var selectedDate: AppDate

    override suspend fun execute(profileId: String, selectedDate: AppDate): LiveData<List<PlannedMedicineItem>> {
        this.profileId = profileId
        this.selectedDate = selectedDate
        val plansLive = planRepo.getLiveByProfile(profileId)
        return getLivePlannedMedicinesItemsForPlans(plansLive)
    }

    private fun getLivePlannedMedicinesItemsForPlans(
        plansLive: LiveData<List<Plan>>
    ) = Transformations.switchMap(plansLive) { plans ->
        val plannedMedicines = getPlannedMedicinesForPlans(plans)
        liveData {
            val value = mapPlannedMedicinesToItems(plannedMedicines)
            emit(value)
        }
    }

    private fun getPlannedMedicinesForPlans(plans: List<Plan>): List<PlannedMedicine> {
        val plannedMedicines = mutableListOf<PlannedMedicine>()
        plans.forEach { singlePlan ->
            val plannedMedicinesForPlan = singlePlan.getPlannedMedicinesForDate(selectedDate)
            plannedMedicines.addAll(plannedMedicinesForPlan)
        }
        return plannedMedicines.toList()
    }

    private suspend fun mapPlannedMedicinesToItems(
        plannedMedicines: List<PlannedMedicine>
    ): List<PlannedMedicineItem> {
        return plannedMedicines.map { singlePlannedMedicine ->
            val medicine = medicineRepo.getById(singlePlannedMedicine.medicineId)!!
            PlannedMedicineItem(singlePlannedMedicine, medicine)
        }
    }
}