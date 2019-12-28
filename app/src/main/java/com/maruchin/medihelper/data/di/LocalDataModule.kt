package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.utils.SharedPref
import org.koin.dsl.module

val localDataModule = module {
    single {
        SharedPref(
            context = get()
        )
    }
}