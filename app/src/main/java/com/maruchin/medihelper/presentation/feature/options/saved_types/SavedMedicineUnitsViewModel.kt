package com.maruchin.medihelper.presentation.feature.options.saved_types

import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedMedicineUnitsUseCase

class SavedMedicineUnitsViewModel(
    private val getLiveSavedMedicineUnitsUseCase: GetLiveSavedMedicineUnitsUseCase
) : SavedTypesViewModel(
    getLiveSavedTypesUseCase = getLiveSavedMedicineUnitsUseCase,
    typesName = "Jednostki leku"
)