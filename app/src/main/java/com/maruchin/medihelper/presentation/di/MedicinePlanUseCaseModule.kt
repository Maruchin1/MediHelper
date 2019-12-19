package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.mediplans.*
import org.koin.dsl.module

val medicinePlanUseCaseModule = module {
    factory { 
        GetLiveMedicinesPlansItemsByProfileUseCase(
            medicinePlanRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        GetMedicinePlanDetailsUseCase(
            medicinePlanRepo = get(),
            medicineRepo = get(),
            profileRepo = get()
        )
    }
    factory {
        GetMedicinePlanEditDataUseCase(
            medicinePlanRepo = get()
        )
    }
    factory { 
        SaveMedicinePlanUseCase(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get(),
            plannedMedicineScheduler = get(),
            deviceCalendar = get()
        )
    }
    factory {
        DeleteMedicinePlanUseCase(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get()
        )
    }
}