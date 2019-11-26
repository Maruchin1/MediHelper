package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.*
import com.maruchin.medihelper.domain.usecases.medicines.GetAllMedicinesItemsLiveUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase
import com.maruchin.medihelper.domain.usecases.medicines.SaveMedicineUseCase
import com.maruchin.medihelper.domain.usecases.user.CreateUserUseCase
import org.koin.dsl.module

val useCasesModule = module {
    factory {
        CreateUserUseCase(
            userRepo = get(),
            profileRepo = get()
        )
    }
    factory {
        GetAllMedicinesItemsLiveUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetMedicineUnitsUseCase(
            medicineRepo = get()
        )
    }
    factory {
        SaveMedicineUseCase(
            medicineRepo = get()
        )
    }
    factory {
        GetMedicineDetailsUseCase(
            medicineRepo = get(),
            profileRepo = get()
        )
    }



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