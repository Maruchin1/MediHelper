package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.plannedmedicines.*
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        GetLivePlannedMedicinesItemsByDateUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        GetPlannedMedicineDetailsUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        ChangePlannedMedicineTakenUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        ChangePlannedMedicineTimeUseCase(
            plannedMedicineRepo = get(),
            deviceReminder = get()
        )
    }
    factory {
        GetPlannedMedicineNotifDataUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get(),
            profileRepo = get()
        )
    }
    factory {
        SetPlannedMedicineTakenUseCase(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        )
    }
    factory {
        NotifyAboutPlannedMedicineUseCase(
            plannedMedicineRepo = get(),
            deviceReminder = get(),
            settingsRepo = get()
        )
    }
    factory {
        ChangePlannedMedicineReminderModeUseCase(
            settingsRepo = get()
        )
    }
    factory {
        GetLivePlannedMedicineReminderModeUseCase(
            settingsRepo = get()
        )
    }
}