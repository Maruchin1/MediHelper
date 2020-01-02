package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.plannedmedicines.*
import com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines.*
import org.koin.dsl.module

val plannedMedicineUseCaseModule = module {
    factory {
        GetLivePlannedMedicinesItemsByDateUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        ) as GetLivePlannedMedicinesItemsByDateUseCase
    }
    factory {
        GetPlannedMedicineDetailsUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        ) as GetPlannedMedicineDetailsUseCase
    }
    factory {
        ChangePlannedMedicineTakenUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        ) as ChangePlannedMedicineTakenUseCase
    }
    factory {
        ChangePlannedMedicineTimeUseCaseImpl(
            plannedMedicineRepo = get(),
            deviceReminder = get()
        ) as ChangePlannedMedicineTimeUseCase
    }
    factory {
        GetPlannedMedicineNotifDataUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get(),
            profileRepo = get()
        ) as GetPlannedMedicineNotifDataUseCase
    }
    factory {
        SetPlannedMedicineTakenUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        ) as SetPlannedMedicineTakenUseCase
    }
    factory {
        NotifyAboutPlannedMedicineUseCaseImpl(
            plannedMedicineRepo = get(),
            deviceReminder = get(),
            settingsRepo = get()
        ) as NotifyAboutPlannedMedicineUseCase
    }
    factory {
        ChangePlannedMedicineReminderModeUseCaseImpl(
            settingsRepo = get()
        ) as ChangePlannedMedicineReminderModeUseCase
    }
    factory {
        GetLivePlannedMedicineReminderModeUseCaseImpl(
            settingsRepo = get()
        ) as GetLivePlannedMedicineReminderModeUseCase
    }
    factory {
        DeletePlannedMedicinesUseCaseImpl(
            plannedMedicineRepo = get(),
            deviceReminder = get()
        ) as DeletePlannedMedicinesUseCase
    }
}