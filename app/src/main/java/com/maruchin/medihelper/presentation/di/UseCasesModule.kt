package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.*
import org.koin.dsl.module

val useCasesModule = module {
    single {
        DateTimeUseCases(calendarApi = get())
    }
    single {
        MedicineUseCases(
            medicineRepo = get(),
            cameraApi = get()
        )
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
            medicineScheduler = get(),
            notificationApi = get()
        )
    }
    single {
        ServerConnectionUseCases(
            appUserRepo = get(),
            personRepo = get()
        )
    }
}