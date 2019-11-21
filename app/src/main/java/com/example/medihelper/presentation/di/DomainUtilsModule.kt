package com.example.medihelper.presentation.di

import com.example.medihelper.domain.utils.MedicineScheduler
import com.example.medihelper.domain.utils.StatusOfTakingCalculator
import org.koin.dsl.module

val domainUtilsModule = module {
    single {
        MedicineScheduler()
    }
    single {
        StatusOfTakingCalculator()
    }
}