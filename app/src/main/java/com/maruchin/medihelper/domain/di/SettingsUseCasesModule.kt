package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.settings.*
import com.maruchin.medihelper.domain.usecasesimpl.settings.*
import org.koin.dsl.module

val settingsUseCasesModule = module {
    factory {
        AreRemindersEnabledUseCaseImpl(
            settingsRepo = get()
        ) as AreRemindersEnabledUseCase

    }
    factory {
        AreLiveRemindersEnabledUseCaseImpl(
            settingsRepo = get()
        ) as AreLiveRemindersEnabledUseCase
    }
    factory {
        SetRemindersEnabledUseCaseImpl(
            settingsRepo = get(),
            deviceReminder = get()
        ) as SetRemindersEnabledUseCase
    }
    factory {
        GetReminderModeUseCaseImpl(
            settingsRepo = get()
        ) as GetReminderModeUseCase
    }
    factory {
        GetLiveReminderModeUseCaseImpl(
            settingsRepo = get()
        ) as GetLiveReminderModeUseCase
    }
    factory {
        SetReminderModeUseCaseImpl(
            settingsRepo = get()
        ) as SetReminderModeUseCase
    }
}