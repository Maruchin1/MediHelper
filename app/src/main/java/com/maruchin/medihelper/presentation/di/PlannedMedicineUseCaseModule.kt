package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTakenUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicinesItemsByDateUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineDetailsUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.UpdateAllPlannedMedicinesStatusUseCase
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
            deviceCalendar = get()
        )
    }
    factory {
        UpdateAllPlannedMedicinesStatusUseCase(
            plannedMedicineRepo = get(),
            deviceCalendar = get()
        )
    }
}