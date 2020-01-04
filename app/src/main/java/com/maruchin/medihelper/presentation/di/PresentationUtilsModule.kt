package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.presentation.utils.LoadingScreen
import com.maruchin.medihelper.presentation.utils.MedicinesSorter
import com.maruchin.medihelper.presentation.utils.PicturesRef
import com.maruchin.medihelper.presentation.utils.SelectedProfile
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
        PicturesRef(
            firebaseStorage = get()
        )
    }
    factory {
        MedicinesSorter()
    }
}