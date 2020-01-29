package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.plans.*
import com.maruchin.medihelper.domain.usecasesimpl.plans.*
import org.koin.dsl.module

val planUseCaseModule = module {
    factory {
        GetLivePlansItemsByProfileUseCaseImpl(
            planRepo = get(),
            medicineRepo = get()
        ) as GetLivePlansItemsByProfileUseCase
    }
    factory {
        GetPlanDetailsUseCaseImpl(
            planRepo = get(),
            medicineRepo = get(),
            profileRepo = get()
        ) as GetPlanDetailsUseCase
    }
    factory {
        GetPlanEditDataUseCaseImpl(
            planRepo = get()
        ) as GetPlanEditDataUseCase
    }
    factory {
        SavePlanUseCaseImpl(
            planRepo = get(),
            validator = get()
        ) as SavePlanUseCase
    }
    factory {
        DeleteSinglePlanUseCaseImpl(
            planRepo = get()
        ) as DeleteSinglePlanUseCase
    }
    factory {
        DeletePlansUseCaseImpl(
            planRepo = get()
        ) as DeletePlansUseCase
    }
    factory {
        GetPlanHistoryUseCaseImpl(
            planRepo = get(),
            deviceCalendar = get()
        ) as GetPlanHistoryUseCase
    }
}