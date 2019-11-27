package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.utils.LoadingScreen
import com.maruchin.medihelper.presentation.utils.SelectedProfile
import org.koin.dsl.module

val utilsModule = module {
    single {
        LoadingScreen()
    }
    single {
        SelectedProfile()
    }
}