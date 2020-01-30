package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.options.reminders.RemindersHelp
import com.maruchin.medihelper.presentation.utils.*
import org.koin.dsl.module

val presentationUtilsModule = module {
    single {
        LoadingScreen()
    }
    single {
        SelectedProfile(
            getMainProfileIdUseCase = get(),
            getProfileColorUseCase = get()
        )
    }
    factory {
        PicturesStorageRef(
            appFirebase = get()
        )
    }
    factory {
        RemindersHelp(
            context = get()
        )
    }
}