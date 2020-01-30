package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.saved_types.*
import com.maruchin.medihelper.domain.usecasesimpl.saved_types.*
import org.koin.dsl.module

val savedTypesUseCaseModule = module {
    factory {
        GetLiveMedicineUnitsUseCaseImpl(
            medicineUnitRepo = get()
        ) as GetLiveMedicineUnitsUseCase
    }
    factory {
        GetLiveMedicineTypesUseCaseImpl(
            medicineTypeRepo = get()
        ) as GetLiveMedicineTypesUseCase
    }
    factory {
        DeleteMedicineTypeUseCaseImpl(
            medicineTypeRepo = get()
        ) as DeleteMedicineTypeUseCase
    }
    factory {
        DeleteMedicineUnitUseCaseImpl(
            medicineUnitRepo = get()
        ) as DeleteMedicineUnitUseCase
    }
    factory {
        AddMedicineUnitUseCaseImpl(
            medicineUnitRepo = get()
        ) as AddMedicineUnitUseCase
    }
    factory {
        AddMedicineTypeUseCaseImpl(
            medicineTypeRepo = get()
        ) as AddMedicineTypeUseCase
    }
}