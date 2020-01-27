package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.planned_medicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.domain.usecases.planned_medicines.GetLivePlannedMedicinesItemsUseCase
import com.maruchin.medihelper.domain.usecasesimpl.planned_medicines.ChangePlannedMedicineTakenUseCaseImpl
import com.maruchin.medihelper.domain.usecasesimpl.planned_medicines.GetLivePlannedMedicinesItemsUseCaseImpl
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        GetLivePlannedMedicinesItemsUseCaseImpl(
            planRepo = get(),
            medicineRepo = get()
        ) as GetLivePlannedMedicinesItemsUseCase
    }
    factory {
        ChangePlannedMedicineTakenUseCaseImpl(

        ) as ChangePlannedMedicineTakenUseCase
    }
}