package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.medicines.*
import org.koin.dsl.module

val medicineUseCaseModule = module {
    factory {
        SaveMedicineUseCase(
            medicineRepo = get()
        )
    }
    factory {
        DeleteMedicineUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetAllMedicinesItemsLiveUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetMedicineUnitsUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetMedicineDetailsUseCase(
            medicineRepo = get(),
            profileRepo = get()
        )
    }
    factory {
        GetMedicineEditDataUseCase(
            medicineRepo = get()
        )
    }
    factory {
        SearchForMedicineInfoUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetMedicineInfoUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetMedicineItemUseCase(
            medicineRepo = get()
        )
    }
}