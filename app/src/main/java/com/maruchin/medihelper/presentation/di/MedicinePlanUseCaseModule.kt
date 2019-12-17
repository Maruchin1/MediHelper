package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.mediplans.GetLiveMedicinesPlansItemsByProfileUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.SaveMedicinePlanUseCase
import org.koin.dsl.module

val medicinePlanUseCaseModule = module {
    factory { 
        GetLiveMedicinesPlansItemsByProfileUseCase(
            medicinePlanRepo = get(),
            medicineRepo = get()
        )
    }
    factory { 
        SaveMedicinePlanUseCase(
            medicinePlanRepo = get()
        )
    }
}