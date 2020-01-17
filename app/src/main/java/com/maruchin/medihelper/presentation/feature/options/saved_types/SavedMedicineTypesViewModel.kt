package com.maruchin.medihelper.presentation.feature.options.saved_types

import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedMedicineTypesUseCase

class SavedMedicineTypesViewModel(
    private val getLiveSavedMedicineTypesUseCase: GetLiveSavedMedicineTypesUseCase
) : SavedTypesViewModel(
    getLiveSavedTypesUseCase = getLiveSavedMedicineTypesUseCase,
    typesName = "Rodzaje leku"
)