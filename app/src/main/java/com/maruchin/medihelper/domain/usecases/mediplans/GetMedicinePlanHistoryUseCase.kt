package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class GetMedicinePlanHistoryUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(medicinePlanId: String): List<HistoryItem> {
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)

        val datesMap = plannedMedicines.groupBy {
            it.plannedDate
        }

        return datesMap.map { mapEntry ->
            HistoryItem(
                date = mapEntry.key,
                checkboxesList = mapEntry.value.map { plannedMedicine ->
                    HistoryItem.CheckBox(
                        plannedMedicineId = plannedMedicine.entityId,
                        plannedTime = plannedMedicine.plannedTime,
                        status = plannedMedicine.status
                    )
                }.sortedBy {
                    it.plannedTime
                }
            )
        }.sortedBy {
            it.date
        }
    }
}