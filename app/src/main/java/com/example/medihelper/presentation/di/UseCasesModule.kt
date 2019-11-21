package com.example.medihelper.presentation.di

import com.example.medihelper.domain.usecases.*
import org.koin.dsl.module

val useCasesModule = module {
    single {
        DateTimeUseCases()
    }
    single {
        MedicineUseCases(medicineRepo = get())
    }
    single {
        PersonUseCases(personRepo = get())
    }
    single {
        MedicinePlanUseCases(
            medicinePlanRepo = get(),
            plannedMedicineUseCases = get()
        )
    }
    single {
        PlannedMedicineUseCases(
            plannedMedicineRepo = get(),
            statusOfTakingCalculator = get(),
            dateTimeUseCases = get(),
            medicineScheduler = get()
        )
    }
    single {
        ServerConnectionUseCases(
            appUserRepo = get(),
            personRepo = get()
        )
    }
}