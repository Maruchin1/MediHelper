package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.*
import com.maruchin.medihelper.domain.usecases.medicines.*
import com.maruchin.medihelper.domain.usecases.profile.GetProfileSimpleItemUseCase
import com.maruchin.medihelper.domain.usecases.user.CreateUserUseCase
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