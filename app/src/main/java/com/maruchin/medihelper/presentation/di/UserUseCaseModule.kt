package com.maruchin.medihelper.presentation.di

import com.maruchin.medihelper.domain.usecases.user.CreateUserUseCase
import org.koin.dsl.module

val userUseCaseModule = module {
    factory {
        CreateUserUseCase(
            userRepo = get(),
            profileRepo = get()
        )
    }
}