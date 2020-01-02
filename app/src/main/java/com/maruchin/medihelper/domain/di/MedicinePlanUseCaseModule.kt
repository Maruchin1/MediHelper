package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.mediplans.*
import com.maruchin.medihelper.domain.usecasesimpl.mediplans.*
import org.koin.dsl.module

val medicinePlanUseCaseModule = module {
    factory { 
        GetLiveMedicinesPlansItemsByProfileUseCaseImpl(
            medicinePlanRepo = get(),
            medicineRepo = get()
        ) as GetLiveMedicinesPlansItemsByProfileUseCase
    }
    factory {
        GetMedicinePlanDetailsUseCaseImpl(
            medicinePlanRepo = get(),
            medicineRepo = get(),
            profileRepo = get()
        ) as GetMedicinePlanDetailsUseCase
    }
    factory {
        GetMedicinePlanEditDataUseCaseImpl(
            medicinePlanRepo = get()
        ) as GetMedicinePlanEditDataUseCase
    }
    factory { 
        SaveMedicinePlanUseCaseImpl(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get(),
            plannedMedicineScheduler = get(),
            deviceCalendar = get(),
            validator = get(),
            deviceReminder = get()
        ) as SaveMedicinePlanUseCase
    }
    factory {
        DeleteSingleMedicinePlanUseCaseImpl(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get(),
            deletePlannedMedicinesUseCase = get()
        ) as DeleteSingleMedicinePlanUseCase
    }
    factory {
        DeleteMedicinesPlansUseCaseImpl(
            medicinePlanRepo = get(),
            plannedMedicineRepo = get(),
            deletePlannedMedicinesUseCase = get()
        )
    }
    factory { 
        GetMedicinePlanHistoryUseCaseImpl(
            plannedMedicineRepo = get()
        ) as GetMedicinePlanHistoryUseCase
    }
}