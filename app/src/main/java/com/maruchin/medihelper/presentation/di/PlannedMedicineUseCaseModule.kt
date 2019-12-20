package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.plannedmedicines.*
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        GetLivePlannedMedicinesItemsByDateUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        GetPlannedMedicineDetailsUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        ChangePlannedMedicineTakenUseCase(
            plannedMedicineRepo = get(),
            deviceCalendar = get()
        )
    }
    factory {
        UpdateAllPlannedMedicinesStatusUseCase(
            plannedMedicineRepo = get(),
            deviceCalendar = get()
        )
    }
    factory {
        ChangePlannedMedicineTimeUseCase(
            plannedMedicineRepo = get()
        )
    }
}