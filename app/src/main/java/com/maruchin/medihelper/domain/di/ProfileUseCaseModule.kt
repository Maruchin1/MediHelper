package com.maruchin.medihelper.domain.di

import com.maruchin.medihelper.domain.usecases.profile.*
import com.maruchin.medihelper.domain.usecasesimpl.profiles.*
import org.koin.dsl.module

val profileUseCaseModule = module {
    factory {
        GetProfileSimpleItemUseCaseImpl(
            profileRepo = get()
        ) as GetProfileSimpleItemUseCase
    }
    factory {
        GetLiveAllProfilesItemsUseCaseImpl(
            profileRepo = get()
        ) as GetLiveAllProfilesItemsUseCase
    }
    factory {
        GetMainProfileIdUseCaseImpl(
            profileRepo = get()
        ) as GetMainProfileIdUseCase
    }
    factory {
        GetProfileColorsUseCaseImpl(
            profileRepo = get()
        ) as GetProfileColorsUseCase
    }
    factory {
        SaveProfileUseCaseImpl(
            profileRepo = get(),
            validator = get()
        ) as SaveProfileUseCase
    }
    factory {
        DeleteProfileUseCaseImpl(
            profileRepo = get(),
            planRepo = get(),
            deletePlansUseCase = get()
        ) as DeleteProfileUseCase
    }
    factory {
        GetProfileEditDataUseCaseImpl(
            profileRepo = get()
        ) as GetProfileEditDataUseCase
    }
    factory {
        GetProfileColorUseCaseImpl(
            profileRepo = get()
        ) as GetProfileColorUseCase
    }
    factory {
        CreateMainProfileUseCaseImpl(
            profileRepo = get()
        ) as CreateMainProfileUseCase
    }
}