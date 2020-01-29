package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.planned_medicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.domain.usecasesimpl.planned_medicines.ChangePlannedMedicineTakenUseCaseImpl
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        ChangePlannedMedicineTakenUseCaseImpl(
            planRepo = get()
        ) as ChangePlannedMedicineTakenUseCase
    }
}