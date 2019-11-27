package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo

class DeleteMedicineUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String) {
        medicineRepo.deleteById(medicineId)
        //todo brak usuwania powiązanych rekordów
    }
}