package com.example.medihelper.presentation.di

import com.example.medihelper.presentation.utils.LoadingScreen
import org.koin.dsl.module

val utilsModule = module {
    single {
        LoadingScreen()
    }
}