package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsUseCase
import com.maruchin.medihelper.domain.usecasesimpl.planned_medicines.GetLivePlannedMedicinesItemsUseCaseImpl
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        GetLivePlannedMedicinesItemsUseCaseImpl(
            planRepo = get(),
            medicineRepo = get()
        ) as GetLivePlannedMedicinesItemsUseCase
    }
}