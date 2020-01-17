package com.maruchin.medihelper.presentation.feature.options.saved_types

import com.maruchin.medihelper.domain.usecases.settings.DeleteSavedMedicineTypeUseCase
import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedMedicineTypesUseCase

class SavedMedicineTypesViewModel(
    private val getLiveSavedMedicineTypesUseCase: GetLiveSavedMedicineTypesUseCase,
    private val deleteSavedMedicineTypeUseCase: DeleteSavedMedicineTypeUseCase
) : SavedTypesViewModel(
    getLiveSavedTypesUseCase = getLiveSavedMedicineTypesUseCase,
    deleteSavedTypeUseCase = deleteSavedMedicineTypeUseCase,
    typesName = "Zapisane rodzaje leku"
)