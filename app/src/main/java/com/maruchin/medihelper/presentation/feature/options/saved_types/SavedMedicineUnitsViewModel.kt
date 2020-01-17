package com.maruchin.medihelper.presentation.feature.options.saved_types

import com.maruchin.medihelper.domain.usecases.settings.DeleteSavedMedicineUnitUseCase
import com.maruchin.medihelper.domain.usecases.settings.GetLiveSavedMedicineUnitsUseCase

class SavedMedicineUnitsViewModel(
    private val getLiveSavedMedicineUnitsUseCase: GetLiveSavedMedicineUnitsUseCase,
    private val deleteSavedMedicineUnitUseCase: DeleteSavedMedicineUnitUseCase
) : SavedTypesViewModel(
    getLiveSavedTypesUseCase = getLiveSavedMedicineUnitsUseCase,
    deleteSavedTypeUseCase = deleteSavedMedicineUnitUseCase,
    typesName = "Zapisane jednostki leku"
)