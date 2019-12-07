package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.*
import org.koin.dsl.module

val useCasesModule = module {

    single {
        DateTimeUseCases(deviceCalendar = get())
    }
    single {
        MedicineUseCases(
            medicineRepo = get(),
            deviceCamera = get()
        )
    }
    single {
        PersonUseCases(profileRepo = get())
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
}