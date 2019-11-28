package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.profile.GetAllProfilesItemsLiveUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetMainProfileIdUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileColorsUseCase
import com.maruchin.medihelper.domain.usecases.profile.GetProfileSimpleItemUseCase
import org.koin.dsl.module

val profileUseCaseModule = module {
    factory {
        GetProfileSimpleItemUseCase(
            profileRepo = get()
        )
    }
    factory {
        GetAllProfilesItemsLiveUseCase(
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
}