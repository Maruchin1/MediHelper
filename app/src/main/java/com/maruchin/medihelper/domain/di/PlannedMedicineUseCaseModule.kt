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
            plannedMedicineRepo = get()
        ) as ChangePlannedMedicineTimeUseCase
    }
    factory {
        SetPlannedMedicineTakenUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get()
        ) as SetPlannedMedicineTakenUseCase
    }
    factory {
        CheckNotTakenMedicinesUseCaseImpl(
            plannedMedicineRepo = get(),
            medicineRepo = get(),
            profileRepo = get(),
            notifications = get()
        ) as CheckNotTakenMedicinesUseCase
    }
    factory {
        DeletePlannedMedicinesUseCaseImpl(
            plannedMedicineRepo = get()
        ) as DeletePlannedMedicinesUseCase
    }
}