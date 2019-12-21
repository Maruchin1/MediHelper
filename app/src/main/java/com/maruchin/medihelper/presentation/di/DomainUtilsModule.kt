package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler
import org.koin.dsl.module

val domainUtilsModule = module {
    single {
        PlannedMedicineScheduler()
    }
}