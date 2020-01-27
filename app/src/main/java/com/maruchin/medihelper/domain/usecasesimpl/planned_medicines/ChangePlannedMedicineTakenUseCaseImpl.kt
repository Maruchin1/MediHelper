package com.maruchin.medihelper.domain.usecasesimpl.planned_medicines

import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.TakenMedicine
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.planned_medicines.ChangePlannedMedicineTakenUseCase

class ChangePlannedMedicineTakenUseCaseImpl(
    private val planRepo: PlanRepo
) : ChangePlannedMedicineTakenUseCase {

    override suspend fun execute(params: ChangePlannedMedicineTakenUseCase.Params) {
        val plan = planRepo.getById(params.planId)!!
        val existingTakenMedicine = findTakenMedicine(plan, params)

        if (plan.takenMedicines.contains(existingTakenMedicine)) {
            plan.takenMedicines.remove(existingTakenMedicine)
        } else {
            val newTakenMedicine = getTakenMedicine(params)
            plan.takenMedicines.add(newTakenMedicine)
        }
        planRepo.update(plan)
    }

    private fun findTakenMedicine(
        plan: Plan,
        params: ChangePlannedMedicineTakenUseCase.Params
    ): TakenMedicine? {
        return plan.takenMedicines.find { takenMedicine ->
            takenMedicine.run {
                plannedDate == params.plannedDate && plannedTime == params.plannedTime
            }
        }
    }

    private fun getTakenMedicine(
        params: ChangePlannedMedicineTakenUseCase.Params
    ) = TakenMedicine(
        plannedDate = params.plannedDate,
        plannedTime = params.plannedTime
    )
}