package com.maruchin.medihelper.data.di

import com.maruchin.medihelper.data.utils.DataSharedPref
import org.koin.dsl.module

val localDataModule = module {
    single {
        DataSharedPref(
            context = get()
        )
    }
}