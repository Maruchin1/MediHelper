package com.maruchin.medihelper.presentation.feature.options.saved_types

import com.maruchin.medihelper.domain.usecases.saved_types.AddMedicineUnitUseCase
import com.maruchin.medihelper.domain.usecases.saved_types.DeleteMedicineUnitUseCase
import com.maruchin.medihelper.domain.usecases.saved_types.GetLiveMedicineUnitsUseCase

class MedicineUnitsViewModel(
    private val getLiveMedicineUnitsUseCase: GetLiveMedicineUnitsUseCase,
    private val deleteMedicineUnitUseCase: DeleteMedicineUnitUseCase,
    private val addMedicineUnitUseCase: AddMedicineUnitUseCase
) : SavedTypesViewModel(
    getLiveSavedTypesUseCase = getLiveMedicineUnitsUseCase,
    deleteSavedTypeUseCase = deleteMedicineUnitUseCase,
    addSavedTypeUseCase = addMedicineUnitUseCase,
    typesName = "Zapisane jednostki leku"
)