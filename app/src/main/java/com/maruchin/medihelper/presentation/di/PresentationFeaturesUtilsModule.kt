package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.feature.medicines_list.MedicinesFilter
import com.maruchin.medihelper.presentation.feature.medicines_list.MedicinesSorter
import org.koin.dsl.module

val presentationFeaturesUtilsModule = module {
    factory {
        MedicinesSorter()
    }
    factory {
        MedicinesFilter()
    }
}