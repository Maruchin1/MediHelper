package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.profile.*
import org.koin.dsl.module

val profileUseCaseModule = module {
    factory {
        GetProfileItemUseCase(
            profileRepo = get()
        )
    }
    factory {
        GetLiveAllProfilesItemsUseCase(
            profileRepo = get()
        )
    }
    factory {
        GetMainProfileIdUseCase(
            profileRepo = get()
        )
    }
    factory {
        GetProfileColorsUseCase(
            profileRepo = get()
        )
    }
    factory {
        SaveProfileUseCase(
            profileRepo = get()
        )
    }
    factory {
        DeleteProfileUseCase(
            profileRepo = get()
        )
    }
}