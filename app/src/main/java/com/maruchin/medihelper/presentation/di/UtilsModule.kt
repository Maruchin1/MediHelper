package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.utils.LoadingScreen
import org.koin.dsl.module

val utilsModule = module {
    single {
        LoadingScreen()
    }
}