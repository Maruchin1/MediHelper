package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.medicines.*
import com.maruchin.medihelper.domain.usecasesimpl.medicines.GetLiveAllMedicinesItemsUseCaseImpl
import com.maruchin.medihelper.domain.usecasesimpl.medicines.*
import org.koin.dsl.module

val medicineUseCaseModule = module {
    factory {
        SaveMedicineUseCaseImpl(
            medicineRepo = get(),
            medicineUnitRepo = get(),
            medicineTypeRepo = get(),
            pictureRepo = get(),
            validator = get()
        ) as SaveMedicineUseCase
    }
    factory {
        DeleteMedicineUseCaseImpl(
            medicineRepo = get(),
            pictureRepo = get(),
            planRepo = get(),
            deletePlansUseCase = get()
        ) as DeleteMedicineUseCase
    }
    factory {
        GetLiveAllMedicinesItemsUseCaseImpl(
            medicineRepo = get()
        ) as GetLiveAllMedicinesItemsUseCase
    }
    factory {
        GetMedicineDefaultsUseCaseImpl(
            medicineUnitRepo = get(),
            medicineTypeRepo = get()
        ) as GetMedicineDefaultsUseCase
    }
    factory {
        GetMedicineDetailsUseCaseImpl(
            medicineRepo = get()
        ) as GetMedicineDetailsUseCase
    }
    factory {
        GetMedicineEditDataUseCaseImpl(
            medicineRepo = get()
        ) as GetMedicineEditDataUseCase
    }
    factory {
        SearchForMedicineInfoUseCaseImpl(
            medicineRepo = get()
        ) as SearchForMedicineInfoUseCase
    }
    factory {
        GetMedicineInfoUseCaseImpl(
            medicineRepo = get()
        ) as GetMedicineInfoUseCase
    }
    factory {
        GetMedicineSimpleItemUseCaseImpl(
            medicineRepo = get()
        ) as GetMedicineSimpleItemUseCase
    }
}