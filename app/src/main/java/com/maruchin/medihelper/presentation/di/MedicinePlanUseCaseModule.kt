package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.mediplans.SaveMedicinePlanContinuousUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.SaveMedicinePlanOnceUseCase
import com.maruchin.medihelper.domain.usecases.mediplans.SaveMedicinePlanPeriodUseCase
import org.koin.dsl.module

val medicinePlanUseCaseModule = module {
    factory {
        SaveMedicinePlanOnceUseCase(
            medicinePlanRepo = get()
        )
    }
    factory {
        SaveMedicinePlanPeriodUseCase(
            medicinePlanRepo = get()
        )
    }
    factory {
        SaveMedicinePlanContinuousUseCase(
            medicinePlanRepo = get()
        )
    }
}