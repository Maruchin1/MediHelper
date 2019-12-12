package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.datetime.CalcDaysRemainUseCase
import com.maruchin.medihelper.domain.usecases.datetime.GetCurrDateUseCase
import org.koin.dsl.module

val dateTimeUseCaseModule = module {
    factory {
        GetCurrDateUseCase(
            deviceCalendar = get()
        )
    }
    factory {
        CalcDaysRemainUseCase(
            deviceCalendar = get()
        )
    }
}