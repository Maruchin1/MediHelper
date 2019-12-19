package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsByDateUseCase
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        GetLivePlannedMedicinesItemsByDateUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
}