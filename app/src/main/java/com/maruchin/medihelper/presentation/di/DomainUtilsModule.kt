package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.utils.MedicineScheduler
import com.maruchin.medihelper.domain.utils.StatusOfTakingCalculator
import org.koin.dsl.module

val domainUtilsModule = module {
    single {
        MedicineScheduler()
    }
    single {
        StatusOfTakingCalculator()
    }
}