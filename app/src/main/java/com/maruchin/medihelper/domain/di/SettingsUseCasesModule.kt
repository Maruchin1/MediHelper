package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.settings.AreLiveNotificationsEnabledUseCase
import com.maruchin.medihelper.domain.usecases.settings.AreNotificationsEnabledUseCase
import com.maruchin.medihelper.domain.usecasesimpl.settings.AreLiveNotificationsEnabledUseCaseImpl
import com.maruchin.medihelper.domain.usecasesimpl.settings.AreNotificationsEnabledUseCaseImpl
import org.koin.dsl.module

val settingsUseCasesModule = module {
    factory {
        AreNotificationsEnabledUseCaseImpl(
            settingsRepo = get()
        ) as AreNotificationsEnabledUseCase

    }
    factory {
        AreLiveNotificationsEnabledUseCaseImpl(
            settingsRepo = get()
        ) as AreLiveNotificationsEnabledUseCase
    }
}