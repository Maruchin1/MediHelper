package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.mediplans.*
import com.maruchin.medihelper.domain.usecasesimpl.mediplans.*
import org.koin.dsl.module

val medicinePlanUseCaseModule = module {
    factory { 
        GetLiveMedicinesPlansItemsByProfileUseCaseImpl(
            medicinePlanRepo = get(),
            medicineRepo = get()
        ) as GetLiveMedicinesPlansItemsByProfileUseCase
    }
    factory {
        GetMedicinePlanDetailsUseCaseImpl(
            medicinePlanRepo = get(),
            medicineRepo = get(),
            profileRepo = get()
        ) as GetMedicinePlanDetailsUseCase
    }
    factory {
        GetMedicinePlanEditDataUseCaseImpl(
            medicinePlanRepo = get()
        ) as GetMedicinePlanEditDataUseCase
    }
    factory { 
        SaveMedicinePlanUseCaseImpl(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get(),
            plannedMedicineScheduler = get(),
            deviceCalendar = get(),
            validator = get(),
            deviceReminder = get()
        ) as SaveMedicinePlanUseCase
    }
    factory {
        DeleteMedicinePlanUseCaseImpl(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get(),
            deviceReminder = get()
        ) as DeleteMedicinePlanUseCase
    }
    factory { 
        GetMedicinePlanHistoryUseCaseImpl(
            plannedMedicineRepo = get()
        ) as GetMedicinePlanHistoryUseCase
    }
}