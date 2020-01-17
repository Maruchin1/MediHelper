package com.maruchin.medihelper.presentation.feature.options.saved_types

import com.maruchin.medihelper.domain.usecases.saved_types.AddMedicineTypeUseCase
import com.maruchin.medihelper.domain.usecases.saved_types.DeleteMedicineTypeUseCase
import com.maruchin.medihelper.domain.usecases.saved_types.GetLiveMedicineTypesUseCase

class MedicineTypesViewModel(
    private val getLiveMedicineTypesUseCase: GetLiveMedicineTypesUseCase,
    private val deleteMedicineTypeUseCase: DeleteMedicineTypeUseCase,
    private val addMedicineTypeUseCase: AddMedicineTypeUseCase
) : SavedTypesViewModel(
    getLiveSavedTypesUseCase = getLiveMedicineTypesUseCase,
    deleteSavedTypeUseCase = deleteMedicineTypeUseCase,
    addSavedTypeUseCase = addMedicineTypeUseCase,
    typesName = "Zapisane rodzaje leku"
)